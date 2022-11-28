package com.tencent.demo.config;

/**
 * 所有动效的类型
 */
public enum TEResourceType {
    MotionRes2D {
        public String getName() {
            return "2dMotionRes";
        }
    }, MotionRes3D {
        public String getName() {
            return "3dMotionRes";
        }
    }, MotionResHand {
        public String getName() {
            return "handMotionRes";
        }
    }, MotionResGan {
        public String getName() {
            return "ganMotionRes";
        }
    }, MotionResMakeup {
        public String getName() {
            return "makeupRes";
        }
    }, MotionResSegment {
        public String getName() {
            return "segmentMotionRes";
        }
    }, Lut {
        public String getName() {
            return "lut";
        }
    };

    public String getName() {
        return "";
    }
}
