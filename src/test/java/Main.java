import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("�������ַ�����");
        Scanner scanner=new Scanner(System.in);
        Main ma = new Main();
        while (scanner.hasNextLine()){
            String str=scanner.nextLine();
            String rex =ma.zipData(str);
            System.out.println(rex);
        }
    }
    public  String zipData(String str){

        if(str==null){
            return str;
        }
        StringBuilder sb= new StringBuilder();
        //�ַ����е�һ���ַ�
        char fChar=str.charAt(0);
        //�ַ�����Ĭ��Ϊ1
        int count=1;
        //�����±��1��ʼ
        for(int i = 1; i < str.length(); i++){
            if("".equals(str)){
                continue;
            }
            if(" ".equals(str)){
                continue;
            }
            char s=str.charAt(i);//�ַ����е�2���ַ�
            if(fChar==s){//�����1���ַ��͵�2����ȣ�����+1
                count++;
            }else{//�����1���ַ��͵�2���ַ������
                if(count>1){//��������1
                    sb.append(fChar);//�ѵ�2���ַ���ֵ����һ���ַ���ֵ׷�ӵ�StringBuilder��
                    sb.append(count);//����׷�ӵ�StringBuilder��
                    count=1;//���³�ʼ������Ϊ1
                }else{//�����1���ַ��͵�2���ַ�����ȣ���������������1���ѵ�һ���ַ�׷�ӵ�StringBuilder��
                    sb.append(fChar);
                }
            }
            fChar=s;//�ѵ�2���ַ���ֵ����һ���ַ�
        }
        sb.append(fChar);//�����ַ�׷�ӵ�StringBuilder��
        if (count>1) {//�����������1����count׷�ӵ�StringBuilder��
            sb.append(count);
        }
        return sb.toString();
    }
}
