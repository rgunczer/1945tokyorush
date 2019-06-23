using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace LevelToJson
{
    class Program
    {
        enum ParserState
        {
            Unknown,
            FindMainObj,
            ReadMainObj,
            ReadTextureObj,
            ReadLevelProperties,
            ReadWayPointProperties,
            ReadCanalProperties,
            ReadIslandProperties,
            ReadPlantProperties,
            ReadPowerUpProperties,
            ReadMoloProperties,
            ReadHutProperties,
            ReadBunkerProperties,
            ReadBunkerTurretProperties,
            ReadOutpostProperties,
            ReadShipProperties,
            ReadAirPlaneProperties,
            ReadFormationProperties,
            ReadFormationAirplaneProperties,
        };

        static void ShowUsage()
        {
            Console.WriteLine("Usage:");
            Console.WriteLine("Path to *.1945 file to load and to convert to json");
        }

        static ParserState FindMainObject(string line)
        {
            switch(line)
            {
                case "@Level": return ParserState.ReadLevelProperties;
                case "@Texture": return ParserState.ReadTextureObj;
                case "@WayPoint": return ParserState.ReadWayPointProperties;
                case "@Canal": return ParserState.ReadCanalProperties;                                    
                case "@Island": return ParserState.ReadIslandProperties;                                    
                case "@Plant": return ParserState.ReadPlantProperties;                                    
                case "@PowerUp": return ParserState.ReadPowerUpProperties;                                   
                case "@Molo": return ParserState.ReadMoloProperties;                                    
                case "@Hut": return ParserState.ReadHutProperties;                                   
                case "@Bunker": return ParserState.ReadBunkerProperties;                                    
                case "@Outpst": return ParserState.ReadOutpostProperties;                                    
                case "@Ship": return ParserState.ReadShipProperties;                                    
                case "@AirPlane": return ParserState.ReadAirPlaneProperties;                                  
                case "@Formation": return ParserState.ReadFormationProperties;
                default: return ParserState.Unknown;                                
            }
        }

        static JObject ConvertToJson(string[] lines)
        {
            JObject currentFormation = null;
            JObject current = new JObject();

            JObject root = new JObject();
            JArray bunkerTurrets = null;            
            JArray formationAirplanes = null;            
                     
            JArray textures = new JArray();
            JArray waypoints = new JArray();
            JArray canals = new JArray();
            JArray islands = new JArray();
            JArray plants = new JArray();
            JArray powerups = new JArray();
            JArray molos = new JArray();
            JArray huts = new JArray();
            JArray bunkers = new JArray();
            JArray outposts = new JArray();
            JArray ships = new JArray();
            JArray airplanes = new JArray();
            JArray formations = new JArray();
            
            root.Add("Textures", textures);
            root.Add("WayPoints", waypoints);
            root.Add("Canals", canals);
            root.Add("Islands", islands);
            root.Add("Plants", plants);
            root.Add("PowerUps", powerups);
            root.Add("Molos", molos);
            root.Add("Huts", huts);
            root.Add("Bunkers", bunkers);
            root.Add("Outposts", outposts);
            root.Add("Ships", ships);
            root.Add("Airplanes", airplanes);
            root.Add("Formations", formations);

            int mainObjCount = 0;
            ParserState parserState = ParserState.FindMainObj;

            string line;
            foreach (string rawline in lines)
            {
                line = rawline.Trim();
                if (line.Length == 0)
                {
                    continue;
                } 
                
                if (line.Length > 0)
                {
                    if (line.Length >= 2)
                    {
                        if (line[0] == '/' && line[1] == '/') // comment line (ignore)
                        {
                            continue;
                        }
                    }
                }

                switch (parserState)
                {
                    case ParserState.FindMainObj:
                        if (line[0] == '@') //  main obj
                        {
                            ParserState ps = FindMainObject(line);
                            if (ps != ParserState.Unknown)
                            {
                                parserState = ps;

                                if (parserState == ParserState.ReadLevelProperties)
                                {                                    
                                    root.Add("Level", current);
                                }
                            }
                        }
                        break;
                      

                    case ParserState.ReadFormationAirplaneProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.ReadFormationProperties;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char[] { '"' });

                                if (key == "type")
                                {                                    
                                    current = new JObject();
                                    current.Add(key, value);
                                    formationAirplanes.Add(current);
                                }

                                if (key == "pos")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }

                                if (key == "vel")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }

                            }
                        }
                        break;

                    case ParserState.ReadFormationProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char[] { '"', '(', ')' });

                                if (key == "id")
                                {
                                    current = new JObject();
                                    current.Add(key, value);
                                    formations.Add(current);

                                    currentFormation = current;
                                    formationAirplanes = new JArray();
                                    currentFormation.Add("airplanes", formationAirplanes);                                    
                                }

                                if (key == "pos")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }
                            }
                        }

                        if (line == "#AirPlane") // sub obj
                        {
                            parserState = ParserState.ReadFormationAirplaneProperties;
                            continue;
                        }
                        break;

                    case ParserState.ReadAirPlaneProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char[] { '"' });

                                if (key == "type")
                                {
                                    current = new JObject();
                                    current.Add(key, value);
                                    airplanes.Add(current);
                                }

                                if (key == "pos")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }

                                if (key == "vel")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }

                            }
                        }
                        break;

                    case ParserState.ReadOutpostProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char[] { '"' });

                                if (key == "type")
                                {
                                    current = new JObject();
                                    current.Add(key, value);
                                    outposts.Add(current);
                                }

                                if (key == "pos")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }
                            }
                        }
                        break;

                    case ParserState.ReadShipProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char[] { '"' });

                                if (key == "type")
                                {
                                    current = new JObject();
                                    current.Add(key, value);
                                    ships.Add(current);
                                }

                                if (key == "pos")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }

                                if (key == "vel")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }

                            }
                        }
                        break;


                    case ParserState.ReadBunkerTurretProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.ReadBunkerProperties;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char[] { '"' });

                                if (key == "pos")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);

                                        JObject ob = new JObject();
                                        ob.Add(key, jarr);                                        
                                        bunkerTurrets.Add(ob);
                                    }
                                }
                            }
                        }
                        break;

                    case ParserState.ReadBunkerProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;                            
                        }

                        if (line == "#Turret") // sub obj
                        {
                            parserState = ParserState.ReadBunkerTurretProperties;
                            continue;
                        }
                        
                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char[] { '"' });

                                if (key == "type")
                                {
                                    current = new JObject();
                                    current.Add(key, value);
                                    bunkerTurrets = new JArray();
                                    current.Add("Turrets", bunkerTurrets);
                                    bunkers.Add(current);
                                }

                                if (key == "pos")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }

                                if (key == "rot")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }

                            }
                        }
                        break;

                    case ParserState.ReadHutProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char[] { '"' });

                                if (key == "type")
                                {
                                    current = new JObject();
                                    current.Add(key, value);
                                    huts.Add(current);
                                }

                                if (key == "pos")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add("pos", jarr);
                                    }
                                }

                                if (key == "vel")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add("vel", jarr);
                                    }
                                }

                            }
                        }
                        break;


                    case ParserState.ReadMoloProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char[] { '"' });

                                if (key == "type")
                                {
                                    current = new JObject();
                                    current.Add(key, value);
                                    molos.Add(current);
                                }

                                if (key == "pos")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add("pos", jarr);
                                    }
                                }

                                if (key == "vel")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add("vel", jarr);
                                    }
                                }

                            }
                        }
                        break;


                    case ParserState.ReadPowerUpProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char[] { '"' });

                                if (key == "type")
                                {
                                    current = new JObject();
                                    current.Add(key, value);
                                    powerups.Add(current);
                                }

                                if (key == "pos")
                                {
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }

                                if (key == "value")
                                {
                                    if (value.Length > 0)
                                    {
                                        current.Add(key, value);
                                    }
                                }
                            }
                        }
                        break;

                    case ParserState.ReadPlantProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char [] { '"' });

                                if (key == "type")
                                {
                                    current = new JObject();
                                    current.Add(key, value);
                                    plants.Add(current);
                                }
                                
                                if (key == "pos")
                                {                                    
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1)) * 2;
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1)) * 2;
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }                                
                            }
                        }                        
                        break;

                    case ParserState.ReadIslandProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char [] { '"' });

                                if (key == "type")
                                {
                                    current = new JObject();
                                    current.Add(key, value);
                                    islands.Add(current);
                                }
                                
                                if (key == "pos")
                                {                                    
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }                                
                            }
                        }                        
                        break;

                    case ParserState.ReadCanalProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char [] { '"' });

                                if (key == "type")
                                {
                                    current = new JObject();
                                    current.Add(key, value);
                                    canals.Add(current);
                                }
                                
                                if (key == "pos")
                                {                                    
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }                                
                            }
                        }                        
                        break;

                    case ParserState.ReadWayPointProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char [] { '"' });

                                if (key == "id")
                                {
                                    current = new JObject();
                                    current.Add(key, value);
                                    waypoints.Add(current);
                                }
                                
                                if (key == "pos")
                                {                                    
                                    string[] arr = value.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                                    if (arr.Length == 2)
                                    {
                                        int x = Convert.ToInt32(arr[0].Substring(1));
                                        int y = Convert.ToInt32(arr[1].Substring(0, arr[1].Length - 1));
                                        JArray jarr = new JArray();
                                        jarr.Add(x);
                                        jarr.Add(y);
                                        current.Add(key, jarr);
                                    }
                                }                                
                            }
                        }                        
                        break;

                    case ParserState.ReadLevelProperties:
                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {                            
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char[] { '"' });
                                current.Add(key, value);                                
                            }
                        }
                        break;

                    case ParserState.ReadTextureObj:

                        if (line[0] == '{') // begin obj
                        {

                        }

                        if (line[0] == '}') // end obj
                        {
                            parserState = ParserState.FindMainObj;
                            current = null;
                        }

                        if (line.Contains('=')) // property
                        {                            
                            string[] comps = line.Split(new char[] { '=' }, StringSplitOptions.RemoveEmptyEntries);
                            if (comps.Length == 2)
                            {
                                string key = comps[0].Trim();
                                string value = comps[1].Trim(new char[] { '"' });

                                textures.Add(value);                                
                            }
                        }
                        break;

                    //Console.WriteLine(line);                        
                }                                    
            }
            Console.WriteLine("Main object count: " + mainObjCount);

            return root;
        }

        static void Main(string[] args)
        {
            if (args.Length == 0)
            {
                ShowUsage();
                return;
            }

            string fullPath = Path.GetFullPath(args[0]);
            string fileName = Path.GetFileNameWithoutExtension(fullPath) + ".json";
            string dirName = Path.GetDirectoryName(fullPath);
            string fullPathToConvertedFile = Path.Combine(dirName, fileName);

            JToken root = ConvertToJson(File.ReadAllLines(fullPath));
            string json = root.ToString();
            Console.WriteLine(json);
            File.WriteAllText(fullPathToConvertedFile, json);
        }
    }
}
