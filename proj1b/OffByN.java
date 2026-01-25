public class OffByN implements CharacterComparator{
    private int n;
    @Override
    public boolean equalChars(char x, char y){
        return (x - y == -n) || (x - y == n);
    }

    public OffByN(int N){
        n = N;
    }

}