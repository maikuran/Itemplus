import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.GZIPOutputStream;
public class StructureNbtGenerator {
  static final int DV=5023;
  static class Block {int x,y,z,state;String name,target,pool;Block(int x,int y,int z,int s){this.x=x;this.y=y;this.z=z;this.state=s;}}
  static void u(DataOutputStream d,String s)throws IOException{d.writeUTF(s);}
  static void i(DataOutputStream d,String n,int v)throws IOException{d.writeByte(3);u(d,n);d.writeInt(v);}
  static void s(DataOutputStream d,String n,String v)throws IOException{d.writeByte(8);u(d,n);u(d,v);}
  static void ints(DataOutputStream d,String n,int... a)throws IOException{d.writeByte(9);u(d,n);d.writeByte(3);d.writeInt(a.length);for(int v:a)d.writeInt(v);}
  static void end(DataOutputStream d)throws IOException{d.writeByte(0);}
  static final String[] PALETTE={"minecraft:air","thesolar:moonstone","thesolar:ancient_lunar_bricks","minecraft:polished_deepslate","thesolar:moon_crystal","minecraft:sea_lantern","minecraft:glass","minecraft:jigsaw","minecraft:jigsaw","minecraft:jigsaw","minecraft:jigsaw","thesolar:lunar_boss_altar","minecraft:chiseled_deepslate","minecraft:iron_block","minecraft:lantern","minecraft:chest","minecraft:bookshelf"};
  static void palette(DataOutputStream d,String[] palette)throws IOException{
    d.writeByte(9);u(d,"palette");d.writeByte(10);d.writeInt(palette.length);
    String[] orientations={"north_up","south_up","west_up","east_up"};
    for(int n=0;n<PALETTE.length;n++){
      s(d,"id",palette[n]);
      if(n>=7&&n<=10){d.writeByte(10);u(d,"properties");s(d,"orientation",orientations[n-7]);end(d);}
      end(d);
    }
  }
  static void chestNbt(DataOutputStream d,String theme)throws IOException{d.writeByte(10);u(d,"nbt");s(d,"id","minecraft:chest");s(d,"LootTable",theme.equals("moon")?"thesolar:chests/lunar_ruins":"thesolar:chests/martian_ruins");d.writeByte(4);u(d,"LootTableSeed");d.writeLong(0L);end(d);}
  static void jigsawNbt(DataOutputStream d,Block b)throws IOException{
    d.writeByte(10);u(d,"nbt");s(d,"id","minecraft:jigsaw");s(d,"name",b.name);s(d,"target",b.target);s(d,"pool",b.pool);s(d,"final_state","minecraft:air");s(d,"joint","rollable");end(d);
  }
  static void blocks(DataOutputStream d,List<Block> bs,String theme)throws IOException{
    d.writeByte(9);u(d,"blocks");d.writeByte(10);d.writeInt(bs.size());
    for(Block b:bs){ints(d,"pos",b.x,b.y,b.z);i(d,"state",b.state);if(b.state>=7&&b.state<=10)jigsawNbt(d,b);else if(b.state==15)chestNbt(d,theme);end(d);}
  }
  static void save(Path out,String theme,String kind,boolean altar)throws Exception{
    Files.createDirectories(out.getParent());
    String rock=theme.equals("moon")?"thesolar:moonstone":"thesolar:martian_rock";
    String brick=theme.equals("moon")?"thesolar:ancient_lunar_bricks":"thesolar:ancient_martian_bricks";
    String accent=theme.equals("moon")?"thesolar:moon_crystal":"thesolar:martian_core";
    String[] p=PALETTE.clone();p[1]=rock;p[2]=brick;p[4]=accent;p[11]=theme.equals("moon")?"thesolar:lunar_boss_altar":"thesolar:martian_boss_altar";
    List<Block> bs=new ArrayList<>();
    for(int x=0;x<17;x++)for(int z=0;z<17;z++){
      bs.add(new Block(x,0,z,2));
      if(x==0||x==16||z==0||z==16)for(int y=1;y<=7;y++){
        boolean door=(x==0||x==16)?(z>=7&&z<=9&&y<=3):(x>=7&&x<=9&&y<=3);
        if(!door)bs.add(new Block(x,y,z,y==7?3:2));
      }
      bs.add(new Block(x,8,z,3));
    }
    for(int x:new int[]{1,15})for(int z:new int[]{1,15})for(int y=1;y<=7;y++)bs.add(new Block(x,y,z,13));
    for(int x:new int[]{3,13})for(int z:new int[]{3,13}){for(int y=1;y<=5;y++)bs.add(new Block(x,y,z,4));bs.add(new Block(x,6,z,5));}
    for(int x=6;x<=10;x++)for(int z=6;z<=10;z++)bs.add(new Block(x,1,z,1));
    for(int x=7;x<=9;x++)for(int z=7;z<=9;z++)bs.add(new Block(x,2,z,12));
    if(altar)bs.add(new Block(8,2,8,11));
    else if(kind.equals("archive")){bs.add(new Block(8,2,8,16));bs.add(new Block(7,2,8,16));bs.add(new Block(9,2,8,16));}
    else if(kind.equals("lab")){bs.add(new Block(8,2,8,4));bs.add(new Block(8,3,8,5));bs.add(new Block(6,2,8,15));bs.add(new Block(10,2,8,15));}
    else if(kind.equals("forge")){bs.add(new Block(8,2,8,4));bs.add(new Block(8,3,8,5));bs.add(new Block(6,2,8,13));bs.add(new Block(10,2,8,13));}
    else if(kind.equals("barracks")){bs.add(new Block(6,2,6,15));bs.add(new Block(10,2,6,15));bs.add(new Block(6,2,10,15));bs.add(new Block(10,2,10,15));}
    else {bs.add(new Block(8,2,8,4));bs.add(new Block(8,3,8,5));}
    for(int x=4;x<=12;x+=2){bs.add(new Block(x,5,1,4));bs.add(new Block(x,5,15,4));bs.add(new Block(1,5,x,4));bs.add(new Block(15,5,x,4));}
    for(int x:new int[]{4,12})for(int z:new int[]{4,12})bs.add(new Block(x,7,z,14));
    bs.add(new Block(3,2,8,15));bs.add(new Block(13,2,8,15));
    String nm=theme.equals("moon")?"thesolar:moon_room_connector":"thesolar:mars_room_connector";
    String pool=theme.equals("moon")?"thesolar:moon_rooms":"thesolar:mars_rooms";
    int[][] ps={{8,2,0,7},{8,2,16,8},{0,2,8,9},{16,2,8,10}};
    for(int[] q:ps){Block b=new Block(q[0],q[1],q[2],q[3]);b.name=nm;b.target=nm;b.pool=pool;bs.add(b);}
    try(DataOutputStream d=new DataOutputStream(new GZIPOutputStream(Files.newOutputStream(out)))){
      d.writeByte(10);u(d,"");i(d,"DataVersion",DV);ints(d,"size",17,9,17);palette(d,p);blocks(d,bs,theme);d.writeByte(9);u(d,"entities");d.writeByte(10);d.writeInt(0);end(d);
    }
  }
  public static void main(String[] a)throws Exception{
    Path r=Path.of(a[0]);
    save(r.resolve("lunar_temple_start.nbt"),"moon","temple",true);save(r.resolve("lunar_research_start.nbt"),"moon","lab",false);save(r.resolve("lunar_city_start.nbt"),"moon","archive",false);save(r.resolve("lunar_guardian_room.nbt"),"moon","guardian",false);save(r.resolve("lunar_archive_room.nbt"),"moon","archive",false);save(r.resolve("lunar_crystal_room.nbt"),"moon","crystal",false);
    save(r.resolve("martian_temple_start.nbt"),"mars","temple",true);save(r.resolve("martian_research_start.nbt"),"mars","lab",false);save(r.resolve("martian_city_start.nbt"),"mars","barracks",false);save(r.resolve("martian_forge_room.nbt"),"mars","forge",false);save(r.resolve("martian_lab_room.nbt"),"mars","lab",false);save(r.resolve("martian_barracks_room.nbt"),"mars","barracks",false);
    System.out.println("Regenerated 12 oriented 26.3 jigsaw templates.");
  }
}
