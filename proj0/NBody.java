public class NBody {
    private static int n;
    private static double radius;
    private static String imgToDraw = "images/starfield.jpg";

    public static double readRadius(String fileName){
        In in = new In(fileName);
        n = in.readInt();
        radius = in.readDouble();
        return radius;
    }

    public static Planet[] readPlanets(String fileName){
        readRadius(fileName);//记得读取radius
        In in = new In(fileName);
        in.readInt();//跳过行数
        in.readDouble();//跳过半径
        Planet[] res = new Planet[n];
        for(int i = 0 ; i < n; i++){
//            double xxPos = in.readDouble();
//            double yyPos = in.readDouble();
//            double xxVel = in.readDouble();
//            double yyVel = in.readDouble();
//            double mass = in.readDouble();
//            String imgFileName = in.readString();
//            res[i] = new Planet(xxPos,yyPos,xxVel,yyVel,mass,imgFileName);
              res[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(),in.readString());
        }
        return res;
    }

    public static void main(String[] args){
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        Planet[] planets = readPlanets(filename);

        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius,radius);
        StdDraw.clear();

        StdAudio.play("audio/2001.mid");


        double time = 0;
        while (time <= T){
            double[] xForces = new double[n];
            double[] yForces = new double[n];

            for(int i = 0; i < n; i++){
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for(int i = 0; i < n; i++){
                planets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0,0,imgToDraw);

            for(Planet planet : planets){
                planet.draw();
            }

            StdDraw.show();
            StdDraw.pause(10);
            time += dt;
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }

}