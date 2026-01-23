public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV,
                  double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet y) {
        double xx = xxPos - y.xxPos;
        double yy = yyPos - y.yyPos;
        return Math.sqrt(xx * xx + yy * yy);
    }

    public double calcForceExertedBy(Planet y) {
        double r = calcDistance(y);
        return (G * mass * y.mass) / (r * r);
    }

    //right is positive
    public double calcForceExertedByX(Planet y) {
        double r = calcDistance(y);
        double force = calcForceExertedBy(y);
        double sin = (y.xxPos - xxPos) / r;
        return force * sin;
    }

    //up is positive
    public double calcForceExertedByY(Planet y) {
        double r = calcDistance(y);
        double force = calcForceExertedBy(y);
        double cos = (y.yyPos - yyPos) / r;
        return force * cos;
    }

    public double calcNetForceExertedByX(Planet[] arr) {
        double forceX = 0;
        for (Planet planet : arr) {
            if (!this.equals(planet))
                forceX += calcForceExertedByX(planet);
        }
        return forceX;
    }

    public double calcNetForceExertedByY(Planet[] arr) {
        double forceY = 0;
        for (Planet planet : arr) {
            if (!this.equals(planet))
                forceY += calcForceExertedByY(planet);
        }
        return forceY;
    }

    public void update(double dt, double fx, double fy) {
        double ax = fx / mass;
        double ay = fy / mass;
        xxVel += ax * dt;
        yyVel += ay * dt;
        xxPos += xxVel * dt;
        yyPos += yyVel * dt;
    }

    public void draw(){
        StdDraw.picture(xxPos,yyPos,"images/"+imgFileName);
    }
}