using System.Drawing;
using System.Linq;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Xml;

namespace PatRec3 {

    public struct Id {
        public int page, line, word;

        public override string ToString() {
            return $"{page}-{line}-{word}";
        }
    }
    
    public class Box : List<Point> {

        public readonly Id Id;

        public readonly int MinX;
        public readonly int MinY;
        public readonly int MaxX;
        public readonly int MaxY;
        
        public int Width { get { return MaxX - MinX; } }

        public int Height { get { return MaxY - MinY; } }
        
        public Box(Id Id, IEnumerable<Point> points) {
            this.Id = Id;
            
            AddRange(points);
            
            MinX = this.Select(p => p.X).Min(); 
            MinY = this.Select(p => p.Y).Min(); 
            MaxX = this.Select(p => p.X).Max(); 
            MaxY = this.Select(p => p.Y).Max(); 
        }
        
        
    }
    
    // Currently gradients are not used.
    public enum Feature { LowerContour = 0, UpperContour, BlackWhiteTransitions, BlackPercent, BlackPercentWithinLcUc, LcGradient, UcGradient }

    public class FeatureVector {
        
        public static readonly int[] weights = {1, 1, 20, 3, 1, 0, 0};
        
        static FeatureVector () {Debug.Assert(weights.Length == Enum.GetNames(typeof(Feature)).Length, "weight vector must have length #featrues.");}

        public int[] elements = new int[Enum.GetValues(typeof(Feature)).Length];

        public int this [Feature feature] {
            get { return elements[(int) feature] / weights[(int) feature]; }
            set { elements[(int) feature] = value * weights[(int) feature]; }
        }
    }

    public static class Algorithms 
    {
        public static List<Box> ParseSVGFile(string path) {
          
            XmlDocument xmlDocument = new XmlDocument();
            xmlDocument.Load(path);
            
            List<Box> boxes = new List<Box>();

            foreach (XmlNode node in xmlDocument.DocumentElement.ChildNodes) {
                
                if (node.Name.Equals("path")) {

                    int[] nums = null;
                    Id id = default(Id);
                    
                    foreach (XmlAttribute att in node.Attributes) {
                
                        if (att.Name.Equals("d")) {
                            nums = att.Value
                                .Split(' ')
                                .Where(s => !s.Equals("L") && !s.Equals("M") && !s.Equals("Z"))
                                .Select(s => (int)Math.Round(Convert.ToDouble(s)))
                                .ToArray();
                        }

                        if (att.Name.Equals("id")) {                            
                            int[] idNums = att.Value
                                .Split('-')
                                .Select(s => Convert.ToInt32(s)).ToArray();
                            id = new Id { page = idNums[0], line = idNums[1], word = idNums[2] };
                        }
                        
                    }
                    
                    Debug.Assert(nums != null && !id.Equals(default(Id)), "nums != null && id != default(Id)");
                    
                    Box b = new Box(
                        id, 
                        nums
                            .Where((n, i) => i % 2 == 0)
                            .Zip(nums.Where((n, i) => i % 2 == 1), (i, j) => new Point(i, j))
                    );
                    
                    boxes.Add(b);
                    
                }
            }
            return boxes;
        }

        private static bool ContainsPoint (this Box box, Point point) {
            
            box.Add(box[0]);

            bool isOdd = false;

            for (int i = 0; i < box.Count - 1; i++) {

                double a1 = box[i].X, a2 = box[i].Y;
                double b1 = box[i + 1].X - a1, b2 = box[i + 1].Y - a2;
                double x1 = point.X, x2 = point.Y;

                double det = (b1 - b2);

                double t = (1 / det) * ((x1 - a1) - (x2 - a2));

                double s = -((1 / det) * (-b2 * (x1 - a1) + b1 * (x2 - a2)));

                if (0 <= t && t < 1 && 0 <= s) {
                    isOdd = !isOdd;
                }
            }
            
            box.RemoveAt(box.Count-1);
            return isOdd;
        }
        
        public static List<Bitmap> SplitToWords(this Bitmap page, List<Box> boxes) {
            
            List<Bitmap> result = new List<Bitmap>();
            
            foreach (Box box in boxes) {

                Bitmap bm = new Bitmap(box.Width, box.Height);
                
                for (int x = 0; x < box.Width; x++) {
                    for (int y = 0; y < box.Height; y++) {
              
                        bm.SetPixel(x, y, 
                            box.ContainsPoint(new Point (x + box.MinX, y + box.MinY)) 
                                ?
                                page.GetPixel(box.MinX + x, box.MinY + y)
                                :
                                Color.White 
                        );
                        
                    }
                }
                
                /* Draws boxes for debugging.
                Graphics g = Graphics.FromImage(bm);
                box.Add(box[0]);
                g.DrawLines(new Pen(Color.Red), 
                    box.Select(p => new PointF {X=p.X - box.MinX, Y=p.Y - box.MinY}).ToArray());
                box.RemoveAt(box.Count-1);
                */

                result.Add(bm);
            }

            return result;
        }

        public static bool[][] Binarize(this Bitmap word) 
        {
            bool[][] result;
            result = new bool [word.Width][];
            
            for (int i = 0; i < result.Length; i++) {
                
                result[i] = new bool[word.Height];
                
                for (int j = 0; j < result[i].Length; j++) {
                    
                    //result[i][j] = (word.GetPixel(i,j).B + word.GetPixel(i,j).R + word.GetPixel(i,j).G) < (3 * cutOffValue);

                    result[i][j] = word.GetPixel(i, j).B < 180;

                }
            }
            return result;
        }

        public static bool[][] Trim(this bool[][] image) {
            
            var array = image.Where(col => col.Any(b => b)).ToArray();
            
            Debug.Assert(array.Length > 0, "word must not be white only.");
            
            int lowest = array.Select(col => col.TakeWhile(b => !b).Count()).Min();
            int highest = array[0].Length - array.Select(col => col.Reverse().TakeWhile(b => !b).Count()).Min();
            int height = highest - lowest;

            for (int i = 0; i < array.Length; i++) {
                array[i] = array[i].Skip(lowest).Take(height).ToArray();
            }

            return array;
        }

        public static bool[][] NormalizeSize(this bool[][] image) {

            const int newWidth = 200, newHeight = 80;
            
            bool [][] result = new bool[newWidth][];

            for (int i = 0; i < newWidth; i++) {
                int col = (int) Math.Round((double) i / (newWidth-1) * (image.Length-1));

                result[i] = new bool[newHeight];
                
                for (int j = 0; j < newHeight; j++) {
                    int row = (int) Math.Round((double) j / (newHeight-1) * (image[0].Length-1));

                    result[i][j] = image[col][row];
                }
               
            }

            return result;
        }

        public static void SaveAsBitmap (this bool[][] binWord, string path) {
            
            Bitmap bm = new Bitmap(binWord.Length, binWord[0].Length);

            for (int i = 0; i < bm.Width; i++) {
                for (int j = 0; j < bm.Height; j++) {
                    bm.SetPixel(i,j,binWord[i][j] ? Color.Black : Color.White);
                }
            }
            
            bm.Save(path);
        }

        public static int FeatureSqDist(FeatureVector fst, FeatureVector snd) {
            return fst.elements.Zip(snd.elements, (i, j) => (i - j) * (i - j)).Sum();
        } 
        
        public static List<FeatureVector> ToFeatureVectors(this bool[][] word) {
            
            List<FeatureVector> result = new List<FeatureVector>(word.Length);

            for (int i = 0; i < word.Length; i++) {
            
                FeatureVector vec = new FeatureVector();

                vec[Feature.BlackWhiteTransitions] = word[i].Skip(1).Where((b, j) => b != word[i][j]).Count();

                vec[Feature.LowerContour] = word[i].TakeWhile(b => !b).Count();

                vec[Feature.UpperContour] = word[i].Length - word[i].Reverse().TakeWhile(b => !b).Count();

                vec[Feature.BlackPercent] = 100 * word[i].Select(b => b ? 1 : 0).Sum() / word[i].Length;

                int height = vec[Feature.UpperContour] - vec[Feature.LowerContour];

                vec[Feature.BlackPercentWithinLcUc] =
                    100 * word[i].Skip(vec[Feature.LowerContour]).Take(height).Select(b => b ? 1 : 0).Sum() / height;
                
                result.Add(vec);

            }

            return result.Where(v => v[Feature.BlackPercent] != 0).ToList();
        }
        
        /* To plot a Feature Vector in R use the following function and this:
        * 
        * dat <- read.table("<name>.csv", sep=",", header=T)
        * ts.plot(dat,gpars= list(col=rainbow(7))) 
        */
        public static void SaveAsCSV(this List<FeatureVector> vectors, string path) {

            string result = String.Join(", ", Enum.GetNames(typeof(Feature)));
            
            foreach (var vec in vectors) {
                result += "\n" + String.Join(", ", vec.elements.Select(i => i.ToString()).ToArray());
            }

            File.WriteAllText(path, result);
        }

        public static Dictionary<Id, List<FeatureVector>> readPages(params int[] pages) {
            var dict = new Dictionary<Id, List<FeatureVector>>();
            foreach (int page in pages) {
                Console.WriteLine($"Reading page {page}...");
                Bitmap pageBitmap = Bitmap.FromFile($"../../data/images/{page}.png") as Bitmap;
                
                Console.WriteLine("   Parsing svg...");
                List<Box> boxes = ParseSVGFile($"../../data/ground-truth/locations/{page}.svg");
                
                Console.WriteLine("   Splitting into words...");
                List<Bitmap> bitmaps = SplitToWords(pageBitmap, boxes);
                
                Console.WriteLine("   Processing bitmaps...");
                List<bool[][]> binarized = bitmaps.Select(Binarize).Select(Trim).Select(NormalizeSize).ToList();

                for (int i = 0; i < binarized.Count; i++) {
                    dict.Add(boxes[i].Id, binarized[i].ToFeatureVectors());
                }
            }
            return dict;
        }

        public static int DTW(List<FeatureVector> fst, List<FeatureVector> snd) {

            int i = 0;
            int j = 0;

            int totalCost = 0;

            while (i < fst.Count - 1 && j < snd.Count - 1) {

                int costIAdvance = FeatureSqDist(fst[i + 1], snd[j]);
                int costJAdvance = FeatureSqDist(fst[i], snd[j + 1]);
                int costIJAdvance = FeatureSqDist(fst[i + 1], snd[j + 1]);

                if (costIAdvance <= costJAdvance && costIAdvance <= costIJAdvance) {
                    totalCost += costIAdvance;
                    i++;
                }
                else if (costJAdvance <= costIJAdvance) {
                    totalCost += costJAdvance;
                    j++;
                }
                else {
                    totalCost += costIJAdvance;
                    i++;
                    j++;
                }
            }

            while (i < fst.Count - 1) {
                totalCost += FeatureSqDist(fst[i + 1], snd[j]);
                i++;
            }

            while (j < snd.Count - 1) {
                totalCost += FeatureSqDist(fst[i], snd[j + 1]);
                j++;
            }

            return totalCost;
        }

        public static IEnumerable<KeyValuePair<Id, int>> FindMostSimilar(
            List<FeatureVector> word,
            Dictionary<Id, List<FeatureVector>> data) 
        {
            Dictionary<Id, int> results = new Dictionary<Id, int>();

            foreach (var kvp in data) {
                results.Add(kvp.Key, DTW(word, kvp.Value));
            }

            return results.OrderBy(kvp => kvp.Value);
        }
    }
    
    internal class Program {

        public static string[] keywords;

        public static Dictionary<Id, string> transcription;

        static Program() {
            transcription = new Dictionary<Id, string>();
            foreach (string[] line in File.ReadAllLines("../../data/ground-truth/transcription.txt").Select(s => s.Split(' '))) {
                int[] idNums = line[0]
                    .Split('-')
                    .Select(s => Convert.ToInt32(s)).ToArray();
                Id id = new Id { page = idNums[0], line = idNums[1], word = idNums[2] };
                transcription.Add(id, line[1]);
            }
            
            keywords = File.ReadAllLines("../../data/task/keywords.txt");
        }
        
        
        public static void Main(string[] args) {

            Dictionary<Id, List<FeatureVector>> data = Algorithms.readPages(270,271,272,273,274,275,276,277,278,279);

            foreach (string keyword in keywords) {
                Id id = transcription.Where(kvp => kvp.Value.Equals(keyword)).Select(kvp => kvp.Key).First();
                Console.WriteLine($"Seaching for {keyword} ({id}):");
                List<FeatureVector> word;
                data.TryGetValue(id, out word);
                var results = Algorithms.FindMostSimilar(word, data).Take(10).Where(kvp => !kvp.Key.Equals(id));
                foreach (var result in results) {
                    string trans;
                    if (transcription.TryGetValue(result.Key, out trans)) {
                        Console.WriteLine($"   Found {trans} ({result.Key}) dissimilarityIndex={result.Value}.");
                    }
                }

            }

            Console.WriteLine("--End");
        }
    }

}