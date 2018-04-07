package org.yamaLab.logFile;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.jnetpcap.packet.PcapPacket;

public class BlockedFileManager extends java.lang.Object
{
    public String fileSeparator;

    public void writeMaxIndex(String dir, long max)
    {
        if(dir==null) return;
        String maxdir=dir+this.fileSeparator+"maxIndex";
        FileOutputStream fouts=null;
        
        try{ fouts= new FileOutputStream(new File(maxdir));}
        catch(FileNotFoundException e){
            System.out.println("wrong directory:"+dir+" ?");
            System.out.println("cannot access"+dir+".");
         }
        
        DataOutputStream dout=null;
        try{
            dout= new DataOutputStream(fouts);
        }
        catch(Exception e){
            System.out.println("wrong directory:"+e.toString()+".");
        }

        try{
            dout.writeLong(max);
        }
        catch(IOException e)
           { System.out.println("save:IOExceptin while flushing.\n");}
           
        try{ dout.close();  }
        catch(IOException e)
           { System.out.println("save:IOException while closing.\n");}
       
    }

    public long readMaxIndex(String dir)
    {
        if(dir==null){
            return 0;
        }
        long x=0;
        String maxdir=dir+this.fileSeparator+"maxIndex";
        DataInputStream inputStream=null;
        try{
            inputStream= //new DataInputStream(url.openStream());
                    new DataInputStream(
                          new FileInputStream(maxdir));
        }
        catch(IOException e){
            System.out.println("url open error.");
            return 0;
        }
         
        try{
            x=inputStream.readLong();
        }
        catch(java.io.IOException e){}

        try{ inputStream.close();  }
        catch(IOException e)
           { System.out.println("read:IOException while closing.");}
        return x;
     }


    public long getMaxIndex()
    {
        return this.maxIndex;
    }


    public String encodingCode;

    public void update()
    {
        if(isBlockOnMemory0Updated){
            writeTheBlockOf(stringsBuffer0,currentBlockOnMemory0);
        }
        if(isBlockOnMemory1Updated){
            writeTheBlockOf(stringsBuffer1,currentBlockOnMemory1);
        }
        if(isBlockOnMemory2Updated){
            writeTheBlockOf(stringsBuffer2,currentBlockOnMemory2);
        }
//        setMaxIndex(maxIndex,true);
        this.writeMaxIndex(this.logBasePath.toString(),this.maxIndex);
    }

    void clearThis()
    {
        String flist[];
        flist=logBasePath.list();
        int number=flist.length;
        System.out.println("files:\n");
        File blocks=new File(logBasePath,"blksize");
        for(int i=0;i<number;i++){
            File afile=new File(logBasePath,flist[i]);
            if(!afile.equals(blocks)){
                if(afile.canWrite()){
                  if(!afile.delete()){
                      System.out.println("failed to delete "+flist[i]);
                  }
                }
                else{
                    System.out.println("can't delete "+flist[i]);
                }
            }
        }
//        setMaxIndex(0,true)
        this.writeMaxIndex(this.logBasePath.toString(),0);
        this.maxIndex=0;
        currentIndex=0;
        currentBlockOnMemory0=0;
        stringsBuffer0=null;
        currentBlockOnMemory1=1;
        stringsBuffer1=null;
        currentBlockOnMemory2=2;
        stringsBuffer2=null;

    }
    long setMaxIndex(int index,boolean isForce)
    {
        return setParameter("maxIndx",index,isForce);
    }
    void close()
    {
        update();
    }
    int maxStringSize;
    Vector currentBlock;
    void putTheBlockOfTheIndex(Vector block,long i)
    {
        long blkIndex=i/blockSize;
        if(blkIndex==currentBlockOnMemory1){
            isBlockOnMemory1Updated=true;
            stringsBuffer1=block;
            return;
        }
        if(blkIndex==currentBlockOnMemory0){

           isBlockOnMemory0Updated=true;
           stringsBuffer0=block;

           if(currentBlockOnMemory0>0){
                if(isBlockOnMemory2Updated){
                    writeTheBlockOf(stringsBuffer2,currentBlockOnMemory2);
                }
                stringsBuffer2=stringsBuffer1;
                stringsBuffer1=block;
                currentBlockOnMemory2=currentBlockOnMemory1;
                currentBlockOnMemory1=currentBlockOnMemory0;
                isBlockOnMemory2Updated=isBlockOnMemory1Updated;
                isBlockOnMemory1Updated=true;
                currentBlockOnMemory0=currentBlockOnMemory0-1;
                stringsBuffer0=readTheBlockOf(currentBlockOnMemory0);
                isBlockOnMemory0Updated=false;
            }
            else{
            }
            return ;
        }
        if(blkIndex==currentBlockOnMemory2){

            isBlockOnMemory2Updated=true;
            stringsBuffer2=block;


            if(isBlockOnMemory0Updated){
                writeTheBlockOf(stringsBuffer0,currentBlockOnMemory0);
            }
            stringsBuffer0=stringsBuffer1;
            stringsBuffer1=block;
            currentBlockOnMemory0=currentBlockOnMemory1;
            currentBlockOnMemory1=currentBlockOnMemory2;
            isBlockOnMemory0Updated=isBlockOnMemory1Updated;
            isBlockOnMemory1Updated=true;
            currentBlockOnMemory2=currentBlockOnMemory2+1;
            stringsBuffer2=readTheBlockOf(currentBlockOnMemory2);
            isBlockOnMemory2Updated=false;

            return ;
        }
   
        if(blkIndex==0){
            currentBlockOnMemory0=0;
            currentBlockOnMemory1=1;
            currentBlockOnMemory2=2;
            stringsBuffer0=readTheBlockOf(currentBlockOnMemory0);
            stringsBuffer1=null;
            stringsBuffer2=null;
            isBlockOnMemory0Updated=false;
//            return stringsBuffer0;
        }
        if(blkIndex>0){
            currentBlockOnMemory1=blkIndex;
            currentBlockOnMemory0=blkIndex-1;
            currentBlockOnMemory2=blkIndex+1;
            stringsBuffer1=readTheBlockOf(currentBlockOnMemory1);
            stringsBuffer0=null;
            stringsBuffer2=null;
            isBlockOnMemory1Updated=false;
//            return stringsBuffer1;
        }
//        return null;
        
   }
    void writeTheBlockOf(Vector block,long i)
    {
//        if(i>maxBlockNumber) maxBlockNumber=i;
        File blockPath=new File(logBasePath,"blk-"+i);
        writeBlock(block,blockPath);
    }
    public Vector readBlock(File path)
    {
        int n1,n2,size=0;
        int i,n=0;
        FileInputStream in=null;
        String s="";
        String s2="";
        byte a[]=null;
        try{
            in=new FileInputStream(path);
        }
        catch(FileNotFoundException e){
            System.out.println("bfm exception:"+e);
            return null;
        }
        DataInputStream din=new DataInputStream(in);
        try{
        	/*
            n1=in.read();
            n2=in.read();
            n=(n1<<8)|(n2 & 0x000000ff);
            */
        	n=din.readInt();
        }
        catch(IOException e){
            System.out.println("bfm exception:"+e);
            return null;
        }
        Vector<PcapPacket> block=new Vector(blockSize);
        block.setSize(blockSize);
        for(i=0;i<n;i++){
            size=0;
            a=new byte[100];
            try{
               size=din.readInt();
                a=new byte[size];
               din.readFully(a,0,size);
            }
            catch(IOException e){
                System.out.println("bfm exception:"+e);
            }
            catch(java.lang.NegativeArraySizeException e){
                System.out.println("bfm exception:"+e);
            }
            PcapPacket m=null;
            if(size>0){
//                System.out.println("i="+i+" size="+size);
            	try{
            	    m=new PcapPacket(a);
            	}
            	catch(Exception e){
            		System.out.println(e.toString()+ " at BlockedFileManager.readBlock i="+i);
//            		Thread.dumpStack();
            	}
            }
            try{
                   block.setElementAt(m,i);
            }
            catch(Exception e){
                System.out.println("bfm exception:"+e+" during setElement.");
            }

        }
        return block;
    }
    Vector getTheBlockOfTheIndex(long i)
    {
        /*
        
                 |
                 +-------------------
                 | blkIndex-1
                 |                      <-- currentBlockOnMemory0
                 |   strinbsBuffer0
                 |
                 +------------------
                 | blkIndex
        i ------>|                      <--- currentBlockOnMemory1
                 |   stringsBuffer1
                 |
                 +-----------------------
                 | blkIndex+1
                 |                      <--- currentBlockOnMemory2
                 |   stringsBuffer2
                 |
                 +-----------------------
                 |
        
        
        */
        Vector rtn;
        int blkIndex=(int)(i/blockSize);
//        System.out.println("getTheBlockOfTheIndex("+i+"), blkIndex="+blkIndex);
        if(blkIndex==currentBlockOnMemory1){
//            System.out.println("blkIndex==currentBlockOnMemory1");
            if(stringsBuffer1!=null) return stringsBuffer1;
            else{
                stringsBuffer1=readTheBlockOf(currentBlockOnMemory1);
                return stringsBuffer1;
            }
        }
        if(blkIndex==currentBlockOnMemory0){
//            System.out.println("blkIndex==currentBlockOnMemory0");
            rtn=stringsBuffer0;
            if(rtn==null){
                   stringsBuffer0=readTheBlockOf(currentBlockOnMemory0);
                   isBlockOnMemory0Updated=false;
                   rtn= stringsBuffer0;
            }
            if(currentBlockOnMemory0>0){
//                System.out.println("currentBlockOnMemory0>0");
                if(isBlockOnMemory2Updated){
                    writeTheBlockOf(stringsBuffer2,currentBlockOnMemory2);
                }
                stringsBuffer2=stringsBuffer1;
                stringsBuffer1=stringsBuffer0;
                currentBlockOnMemory2=currentBlockOnMemory1;
                currentBlockOnMemory1=currentBlockOnMemory0;
                currentBlockOnMemory0=currentBlockOnMemory0-1;
                stringsBuffer0=readTheBlockOf(currentBlockOnMemory0);
                isBlockOnMemory0Updated=false;
                isBlockOnMemory2Updated=isBlockOnMemory1Updated;
                isBlockOnMemory1Updated=isBlockOnMemory0Updated;
            }
            return rtn;
        }
        if(blkIndex==currentBlockOnMemory2){
//            System.out.println("blkIndex==currentBlockOnMemory2");
            rtn=stringsBuffer2;
            if(rtn==null){
                    stringsBuffer2=readTheBlockOf(currentBlockOnMemory2);
                    isBlockOnMemory2Updated=false;
                    rtn= stringsBuffer2;
            }
//            if(currentBlockOnMemory2<maxBlockNumber){
//                System.out.println("currentBlockOnMemory2<maxBlockNumber");
                if(isBlockOnMemory0Updated){
                    writeTheBlockOf(stringsBuffer0,currentBlockOnMemory0);
                }
                currentBlockOnMemory0=currentBlockOnMemory1;
                currentBlockOnMemory1=currentBlockOnMemory2;
                currentBlockOnMemory2=currentBlockOnMemory2+1;
                stringsBuffer0=stringsBuffer1;
                stringsBuffer1=stringsBuffer2;
                stringsBuffer2=readTheBlockOf(currentBlockOnMemory2);
                isBlockOnMemory0Updated=isBlockOnMemory1Updated;
                isBlockOnMemory1Updated=isBlockOnMemory2Updated;
                isBlockOnMemory2Updated=false;
//            }
            return rtn;
        }
//            System.out.println("blkIndex==?");

        if(blkIndex==0){
            currentBlockOnMemory0=0;
            currentBlockOnMemory1=1;
            currentBlockOnMemory2=2;
            stringsBuffer0=readTheBlockOf(currentBlockOnMemory0);
            stringsBuffer1=null;
            stringsBuffer2=null;
            isBlockOnMemory0Updated=false;
            return stringsBuffer0;
        }
        if(blkIndex>0){
            currentBlockOnMemory1=blkIndex;
            currentBlockOnMemory0=blkIndex-1;
            currentBlockOnMemory2=blkIndex+1;
            stringsBuffer1=readTheBlockOf(currentBlockOnMemory1);
            stringsBuffer0=null;
            stringsBuffer2=null;
            isBlockOnMemory1Updated=false;
            return stringsBuffer1;
        }
        
        return null;
    }
    int indexInTheBlock(long i)
    {
        return (int)(i%blockSize);
    }
    int setParameter(String name,int x, boolean isSetToTheFile)
    {
        int rtn=0;
        byte xx[]=new byte[50];
        String sx=""+x;
        sx.getBytes(0,sx.length(),xx,1);
        xx[0]=(byte)(sx.length());
        File parameterPath=new File(logBasePath,name);
        if(parameterPath.exists()){
            if(isSetToTheFile){
               try{
                   FileOutputStream out=new FileOutputStream(parameterPath);
                   out.write(xx,0,sx.length()+1);
                   out.close();
               }
               catch(IOException e){
               }
               rtn=x;
            }
            else{
               try{
                   FileInputStream in=new FileInputStream(parameterPath);
                   int len=in.read();
                   in.read(xx,1,len);
                   if(len<0){
                   }
                   in.close();
                   xx[0]=(byte)len;
                   sx=new String(xx,0,1,len);
                   rtn=((new Integer(sx)).intValue());
               }
               catch(IOException e){
               }
            }
        }
        else{
               try{
                   FileOutputStream out=new FileOutputStream(parameterPath);
                   out.write(xx,0,sx.length()+1);
                   out.close();
               }
               catch(IOException e){
               }
               rtn=x;
        }
        return rtn;
    }
    File logBasePath;
    void setBlockSize(int size)
    {
        blockSize=setParameter("blksize",size,false);
    }
    void init(String dir)
    {
        this.fileSeparator=System.getProperty("file.separator");
        blockSize=2000;
        maxIndex=0;
        logBasePath=new File(dir);
        if(!logBasePath.exists()){
            logBasePath.mkdir();
            setBlockSize(blockSize);
            this.writeMaxIndex(this.logBasePath.toString(),0);
        }
        else{
            if(!logBasePath.isDirectory()){
                System.out.println("Opening "+dir+", it is not directory.");
                return;
            }
            setBlockSize(blockSize);
//           maxIndex=setMaxIndex(maxIndex,false);
//            this.writeMaxIndex(logBasePath.toString(),maxIndex);
            this.maxIndex=this.readMaxIndex(logBasePath.toString());
        }
        currentIndex=0;
        currentBlockOnMemory0=0;
        stringsBuffer0=null;
        currentBlockOnMemory1=1;
        stringsBuffer1=null;
        currentBlockOnMemory2=2;
        stringsBuffer2=null;
        maxStringSize=2100;
     }
    public BlockedFileManager()
    {
        init("tempLog");
    }
    public BlockedFileManager(String x)
    {
        init(x);
    }
    long currentIndex;
    PcapPacket getMessageAt(long i)
    {
        if(i>=this.maxIndex) return null;
        Vector theBlock=getTheBlockOfTheIndex(i);
        if(theBlock==null) return null;
        PcapPacket rtn= (PcapPacket)(theBlock.elementAt(indexInTheBlock(i)));
        currentIndex=i;
        return rtn;
    }
    public synchronized void putMessageAt(PcapPacket s,long i)
    {
        Vector currentBlock=getTheBlockOfTheIndex(i);
        if(currentBlock==null){
            currentBlock=new Vector((int)blockSize);
            currentBlock.setSize((int)blockSize);
        }
        currentBlock.setElementAt(s,indexInTheBlock(i));
        putTheBlockOfTheIndex(currentBlock,i);
        currentIndex=i;
        if(i>=this.maxIndex) this.maxIndex=i+1;
        notifyAll();
    }
    boolean writeBlock(Vector block, File path)
    {
        // n: the number of blocks in this path
        if(block==null) return false;
        if(path==null) return false;
//        System.out.println("start writeBlock path="+path.toString());
        int n=0,i,n1,n2;
        FileOutputStream out=null;
        try{
           out=new FileOutputStream(path);
        }
        catch(IOException e){
            return false;
        }
        DataOutputStream dout=null;
        try{
            dout= new DataOutputStream(out);
        }
        catch(Exception e){
            System.out.println("wrong directory:"+e.toString()+".");
        }
        try{
            n=block.size();
            /*
            n1=n>>8;
            n2=n&0x000000ff;
            out.write(n1);
            out.write(n2);
            */
            dout.writeInt(n);
        }
        catch(IOException e){
            return false;
        }
        for(i=0;i<n;i++){
//            int size;
           PcapPacket s=(PcapPacket)(block.elementAt(i));
//           byte[] lb=new byte[4];
           if(s!=null){
        	   int size=s.getTotalSize();
//               System.out.println("i="+i+" size="+size);
        	   byte[] a=new byte[size];
                 s.transferStateAndDataTo(a);
                 /*
                 lb[0]=(byte)((size>>24)&0x000000ff);
                 lb[1]=(byte)((size>>16)&0x000000ff);
                 lb[0]=(byte)((size>>8)&0x000000ff);
                 lb[1]=(byte)(size&0x000000ff);
                 */
                 try{
//                    out.write(lb);
                	 dout.writeInt(size);
                    out.write(a,0,size);
                 }
                 catch(IOException e){
                    return false;
                 }
            }
            else{
//                lb[0]=0; lb[1]=0; lb[2]=0; lb[3]=0;
                try{
//                  out.write(lb);
                	dout.writeInt(0);
                }
                catch(IOException e){
                    return false;
                }
            }
        }
//        System.out.println("end writeBlock");
        return true;
    }
    Vector readTheBlockOf(long i)
    {
        Vector rtn;
        File blockPath=new File(logBasePath,"blk-"+i);
        if(blockPath.exists()){
            rtn=readBlock(blockPath);
            return rtn;
        }
        else{
            return null;
        }
    }
    void comment()
    {
        /*
            File codeBasePath
            File store

            int blockSize



            appendString(String s)

                chose the block to append the s.

                append the s to the block.

                   if the block




        */
    }
    boolean isBlockOnMemory2Updated;
    boolean isBlockOnMemory1Updated;
    boolean isBlockOnMemory0Updated;
    long maxIndex;
    long maxBlockNumber;
    long currentStringIndex;
    long currentBlockOnMemory2;
    long currentBlockOnMemory1;
    long currentBlockOnMemory0;
    long currentStringsBuffer;
    File tempRecords;
    File store;
    File codeBasePath;
    long blockNumber;
    int blockSize;
    Vector stringsBuffer2;
    Vector stringsBuffer1;
    public Vector stringsBuffer0;
}

