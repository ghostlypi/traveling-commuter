package main;

public class Service {

    public static enum BART {
        RED(0),
        ORANGE(1),
        YELLOW(2),
        GREEN(3),
        BLUE(4),
        EBART(5);

        public final int code;

        private BART(int code) {
            this.code = code;
        }
    }

    public static enum MUNI {
        J(6),
        K(7),
        L(8),
        M(9),
        N(10),
        T(11);

        public final int code;

        private MUNI(int code) {
            this.code = code;
        }
    }

    public static enum VTA {
        ORANGE(12),
        GREEN(13),
        BLUE(14);

        public final int code;

        private VTA(int code) {
            this.code = code;
        }
    }

    public static enum CALTRAIN {
        GILROY(15),
        COLLEGEPARK(16),
        LOCALTAMIEN(17),
        LOCAL(18),
        LIMITED(19),
        EXPRESS(20);

        public final int code;

        private CALTRAIN(int code) {
            this.code = code;
        }
    }

    public static enum SMART {
        SMART(21),
        FERRY(22);

        public final int code;

        private SMART(int code) {
            this.code = code;
        }
    }
}
