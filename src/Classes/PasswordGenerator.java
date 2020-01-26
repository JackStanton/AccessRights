package Classes;

public class PasswordGenerator {

    private  StringBuilder password;

    public PasswordGenerator(int size){
        password = new StringBuilder();
        for (int i = 0; i < size; i++) {
            passwordGen();
        }
    }

    private void passwordGen(){
        double init = Math.random()*100;
        if (init < 50){
            int min = 65;
            int max = 90;
            int random = min + (int) (Math.random() * (max-min));
            password.append((char)random);
        } else {
            int min = 97;
            int max = 122;
            int random = min + (int) (Math.random() * (max-min));
            password.append((char)random);
        }
    }

    public  StringBuilder getPassword() {
        return password;
    }
}
