package main;

public class Service {

    public static final int ERROR = -1;

    public static class BART extends Service {
        public static final int RED = 0;
        public static final int ORANGE = 1;
        public static final int YELLOW = 2;
        public static final int GREEN = 3;
        public static final int BLUE = 4;

        private BART(int code) {
            super(code);
        }
    }

    public static class MUNI extends Service {
        public static final int J = 5;
        public static final int K = 6;
        public static final int L = 7;
        public static final int M = 8;
        public static final int N = 9;
        public static final int T = 10;

        private MUNI(int code) {
            super(code);
        }
    }

    public static class VTA extends Service {
        public static final int ORANGE = 11;
        public static final int GREEN = 12;
        public static final int BLUE = 13;

        private VTA(int code) {
            super(code);
        }
    }

    public static class CALTRAIN extends Service {
        public static final int GILROY = 14;
        public static final int LOCAL = 15;
        public static final int LIMITED = 16;
        public static final int EXPRESS = 17;

        private CALTRAIN(int code) {
            super(code);
        }
    }

    public static class SMART extends Service {
        public static final int SMART = 18;
        public static final int FERRY = 19;

        private SMART(int code) {
            super(code);
        }
    }

    public final int code;

    public Service(int code) {
        this.code = code;
    }

    public boolean isMuni() {
        return  4 < code && code < 11;
    }

    public boolean equals(Object o) {
        if (o instanceof Service)
            return ((Service) o).code == code;
        else if (o instanceof Integer)
            return ((Integer) o) == code;
        return false;
    }

    public String toString() {
        switch (code) {
            case VTA.ORANGE:
                return "VTA.ORANGE";
            default:
                return "ERROR";
        }
    }

    public static Service parse_service(String provider, String line) {
        switch (provider) {
            case "VTA":
                switch (line) {
                    case "ORANGE LINE":
                        return new Service(VTA.ORANGE);
                    case "GREEN LINE":
                        return new Service(VTA.GREEN);
                    case "BLUE LINE":
                        return new Service(VTA.BLUE);
                    default:
                        return new Service(Service.ERROR);
                }
            case "BART":
                switch (line) {
                    case "BLUE-N":
                        return new Service(BART.BLUE);
                    case "BLUE-S":
                        return new Service(BART.BLUE);
                    case "YELLOW-S":
                        return new Service(BART.YELLOW);
                    case "YELLOW-N":
                        return new Service(BART.YELLOW);
                    case "ORANGE-N":
                        return new Service(BART.ORANGE);
                    case "ORANGE-S":
                        return new Service(BART.ORANGE);
                    case "RED-S":
                        return new Service(BART.RED);
                    case "RED-N":
                        return new Service(BART.RED);
                    case "GREEN-N":
                        return new Service(BART.GREEN);
                    case "GREEN-S":
                        return new Service(BART.GREEN);
                    default:
                        return new Service(Service.ERROR);
                }
            case "CALTRAIN":
                switch (line) {
                    case "LIMITED":
                        return new Service(CALTRAIN.LIMITED);
                    case "SOUTH COUNTY":
                        return new Service(CALTRAIN.GILROY);
                    case "LOCAL WEEKDAY":
                        return new Service(CALTRAIN.LOCAL);
                    case "EXPRESS":
                        return new Service(CALTRAIN.EXPRESS);
                    default:
                        return new Service(Service.ERROR);
                }
            case "MUNI":
                switch (line) {
                    case "J":
                        return new Service(MUNI.J);
                    case "K":
                        return new Service(MUNI.K);
                    case "L":
                        return new Service(MUNI.L);
                    case "M":
                        return new Service(MUNI.M);
                    case "N":
                        return new Service(MUNI.N);
                    case "T":
                        return new Service(MUNI.T);
                    default:
                        return new Service(Service.ERROR);
                }
            default:
                return new Service(Service.ERROR);
        }
    }
}
