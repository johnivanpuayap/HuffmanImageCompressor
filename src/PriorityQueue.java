public class PriorityQueue{
    HuffmanNode[] list = new HuffmanNode[0];

    public void enqueue(HuffmanNode key){
        boolean added = false;
        for(int i = 0; i < list.length; i++){
            if(list[i].ctr > key.ctr){
                HuffmanNode[] buffer = new HuffmanNode[list.length+1];
                for(int j = 0, a = 0; j < list.length; j++, a++){
                    if(j == i){
                        buffer[a++] = key;
                        added = true;
                    }
                    buffer[a] = list[j];
                }
                list = null;
                list = buffer;
                buffer = null;
                return;
            }
        }

        if(!added){
            HuffmanNode[] buffer = new HuffmanNode[list.length+1];
            System.arraycopy(list, 0, buffer, 0, list.length);
            buffer[list.length] = key;
            list = null;
            list = new HuffmanNode[buffer.length];
            System.arraycopy(buffer, 0, list, 0, buffer.length);
            buffer = null;
            return;
        }
    }

    public HuffmanNode dequeue(){
        HuffmanNode ret = null;
        if(list.length == 0){
            return null;
        }else{
            HuffmanNode[] buffer = new HuffmanNode[list.length-1];
            ret = list[0];
            ret.ctr = list[0].ctr;
            System.arraycopy(list, 1, buffer, 0, buffer.length);
            list = null;
            list = buffer;
            buffer = null;
            return ret;
        }
    }
}