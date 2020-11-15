public class Point {

    private int x;
    private int y;

    public Point(String point) {
        setXY(point);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private void setXY(String point){
        String[] coordinate = point.split("");
        this.x = coordinate[0].charAt(0) - 65;
        this.y = Integer.parseInt(coordinate[1]);
    }
}
