import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
public class StructureNbtValidator {
  static Object payload(DataInputStream d,int type)throws Exception{
    return switch(type){
      case 1 -> d.readByte(); case 2 -> d.readShort(); case 3 -> d.readInt(); case 4 -> d.readLong();
      case 5 -> d.readFloat(); case 6 -> d.readDouble(); case 7 -> {int n=d.readInt();byte[] b=new byte[n];d.readFully(b);yield b;}
      case 8 -> d.readUTF();
      case 9 -> {int t=d.readUnsignedByte(),n=d.readInt();List<Object> a=new ArrayList<>(n);for(int i=0;i<n;i++)a.add(payload(d,t));yield a;}
      case 10 -> {Map<String,Object> m=new LinkedHashMap<>();while(true){int t=d.readUnsignedByte();if(t==0)break;String k=d.readUTF();m.put(k,payload(d,t));}yield m;}
      case 11 -> {int n=d.readInt();int[] a=new int[n];for(int i=0;i<n;i++)a[i]=d.readInt();yield a;}
      case 12 -> {int n=d.readInt();long[] a=new long[n];for(int i=0;i<n;i++)a[i]=d.readLong();yield a;}
      default -> throw new IOException("Unknown NBT tag type "+type);
    };
  }
  @SuppressWarnings("unchecked") static Map<String,Object> map(Object o){return (Map<String,Object>)o;}
  @SuppressWarnings("unchecked") static List<Object> list(Object o){return (List<Object>)o;}
  static void validate(Path file)throws Exception{
    try(DataInputStream d=new DataInputStream(new GZIPInputStream(Files.newInputStream(file)))){
      if(d.readUnsignedByte()!=10)throw new IOException("Root is not compound");d.readUTF();Map<String,Object> root=map(payload(d,10));
      if(!Integer.valueOf(5023).equals(root.get("DataVersion")))throw new IOException("Wrong DataVersion");
      List<Object> size=list(root.get("size"));int sx=(Integer)size.get(0),sy=(Integer)size.get(1),sz=(Integer)size.get(2);
      List<Object> palette=list(root.get("palette")),blocks=list(root.get("blocks"));
      for(Object entry:palette)if(!map(entry).containsKey("id"))throw new IOException("Palette must use 26.3 id field");
      int jigsaws=0,chests=0;
      for(Object item:blocks){Map<String,Object> b=map(item);List<Object> pos=list(b.get("pos"));int x=(Integer)pos.get(0),y=(Integer)pos.get(1),z=(Integer)pos.get(2);int state=(Integer)b.get("state");
        if(x<0||x>=sx||y<0||y>=sy||z<0||z>=sz||state<0||state>=palette.size())throw new IOException("Invalid block at "+x+","+y+","+z+" state="+state);
        if(state>=7&&state<=10){jigsaws++;Map<String,Object> n=map(b.get("nbt"));if(!n.containsKey("name")||!n.containsKey("target")||!n.containsKey("pool")||!n.containsKey("final_state"))throw new IOException("Incomplete jigsaw NBT");Map<String,Object> props=map(map(palette.get(state)).get("properties"));if(!props.containsKey("orientation"))throw new IOException("Jigsaw orientation missing");}
        if(state==15){chests++;Map<String,Object> n=map(b.get("nbt"));String loot=String.valueOf(n.get("LootTable"));if(!loot.startsWith("thesolar:chests/"))throw new IOException("Chest loot table missing");Path lootFile=file.getParent().getParent().resolve("loot_table").resolve(loot.substring("thesolar:".length())+".json");if(!Files.exists(lootFile))throw new IOException("Unresolved chest loot table "+loot);}
      }
      if(jigsaws!=4||chests<2)throw new IOException("Expected 4 oriented jigsaws + at least 2 loot chests; got "+jigsaws+" + "+chests);
      System.out.println(file.getFileName()+": PASS size="+sx+"x"+sy+"x"+sz+" palette="+palette.size()+" blocks="+blocks.size()+" jigsaws="+jigsaws+" chests="+chests);
    }
  }
  public static void main(String[] args)throws Exception{int n=0;try(var s=Files.list(Path.of(args[0]))){for(Path p:s.filter(x->x.toString().endsWith(".nbt")).sorted().toList()){validate(p);n++;}}System.out.println("Validated "+n+" structure NBT templates.");}
}
