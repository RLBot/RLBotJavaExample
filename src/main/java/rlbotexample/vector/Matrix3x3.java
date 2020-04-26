package rlbotexample.vector;

import java.util.Arrays;

public class Matrix3x3 {
    private final float[] data = new float[9];

    public Matrix3x3() {
        Arrays.fill(data, 0);
    }

    public Matrix3x3(Matrix3x3 other) {
        System.arraycopy(other.data, 0, this.data, 0, data.length);
    }

    
    public static Matrix3x3 identity() {
        Matrix3x3 mat = new Matrix3x3();
        mat.assign(0, 0, 1);
        mat.assign(1, 1, 1);
        mat.assign(2, 2, 1);
        return mat;
    }

  
    public static Matrix3x3 antiSym( Vector3 w) {
        // http://mathworld.wolfram.com/AntisymmetricMatrix.html

        Matrix3x3 mat = new Matrix3x3();
        mat.assign(0, 1, -w.z);
        mat.assign(1, 0, w.z);

        mat.assign(2, 0, -w.y);
        mat.assign(0, 2, w.y);

        mat.assign(1, 2, -w.x);
        mat.assign(2, 1, w.x);
        return mat;
    }

   
    public static Matrix3x3 R3_basis(Vector3 n) {
        float sign = n.z >= 0f ? 1f : -1f;
        float a = -1f / (sign + n.z);
        float b = n.x * n.y * a;

        Matrix3x3 mat = new Matrix3x3();

        mat.assign(0, 0, 1f + sign * n.x * n.x * a);
        mat.assign(0, 1, b);
        mat.assign(0, 2, n.x);

        mat.assign(1, 0, sign * b);
        mat.assign(1, 1, sign + n.y * n.y * a);
        mat.assign(1, 2, n.y);

        mat.assign(2, 0, -sign * n.x);
        mat.assign(2, 1, -n.y);
        mat.assign(2, 2, n.z);

        return mat;
    }

 
    public static Matrix3x3 axisToRotation(Vector3 omega) {
        float norm_omega = (float) omega.magnitude();

        if (Math.abs(norm_omega) == 0)
            norm_omega = 1.1755e-38f;
        {
            Vector3 u = omega.div(norm_omega);

            float c = (float) Math.cos(norm_omega);
            float s = (float) Math.sin(norm_omega);

            Matrix3x3 mat = new Matrix3x3();

            mat.assign(0, 0, u.get(0) * u.get(0) * (1.0f - c) + c);
            mat.assign(0, 1, u.get(0) * u.get(1) * (1.0f - c) - u.get(2) * s);
            mat.assign(0, 2, u.get(0) * u.get(2) * (1.0f - c) + u.get(1) * s);

            mat.assign(1, 0, u.get(1) * u.get(0) * (1.0f - c) + u.get(2) * s);
            mat.assign(1, 1, u.get(1) * u.get(1) * (1.0f - c) + c);
            mat.assign(1, 2, u.get(1) * u.get(2) * (1.0f - c) - u.get(0) * s);

            mat.assign(2, 0, u.get(2) * u.get(0) * (1.0f - c) - u.get(1) * s);
            mat.assign(2, 1, u.get(2) * u.get(1) * (1.0f - c) + u.get(0) * s);
            mat.assign(2, 2, u.get(2) * u.get(2) * (1.0f - c) + c);

            return mat;
        }
    }


    public static Matrix3x3 eulerToRotation(Vector3 pyr) {
        Matrix3x3 mat = new Matrix3x3();
        float CP = (float) Math.cos(pyr.x);
        float SP = (float) Math.sin(pyr.x);
        float CY = (float) Math.cos(pyr.y);
        float SY = (float) Math.sin(pyr.y);
        float CR = (float) Math.cos(pyr.z);
        float SR = (float) Math.sin(pyr.z);

        mat.assign(0, 0, CP * CY);
        mat.assign(0, 0, CP * CY);
        mat.assign(1, 0, CP * SY);
        mat.assign(2, 0, SP);

        mat.assign(0, 1, CY * SP * SR - CR * SY);
        mat.assign(1, 1, SY * SP * SR + CR * CY);
        mat.assign(2, 1, -CP * SR);

        mat.assign(0, 2, -CR * CY * SP - SR * SY);
        mat.assign(1, 2, -CR * SY * SP + SR * CY);
        mat.assign(2, 2, CP * CR);

        return mat;
    }

    
    public static Matrix3x3 from( Vector3 forward,  Vector3 up,  Vector3 left) {
        Matrix3x3 mat = new Matrix3x3();

        mat.assign(0, 0, forward.x);
        mat.assign(1, 0, forward.y);
        mat.assign(2, 0, forward.z);

        mat.assign(0, 1, left.x);
        mat.assign(1, 1, left.y);
        mat.assign(2, 1, left.z);

        mat.assign(0, 2, up.x);
        mat.assign(1, 2, up.y);
        mat.assign(2, 2, up.z);

        return mat;
    }

    
    public static Matrix3x3 lookAt(Vector3 direction, Vector3 up) {
        if (up == null)
            up = new Vector3(0, 0, 1);

        Vector3 f = direction.normalized();
        Vector3 u = f.crossProduct(up.crossProduct(f)).normalized();
        Vector3 l = u.crossProduct(f).normalized();

        Matrix3x3 mat = new Matrix3x3();
        mat.assign(0, 0, f.x);
        mat.assign(0, 1, l.x);
        mat.assign(0, 2, u.x);

        mat.assign(1, 0, f.y);
        mat.assign(1, 1, l.y);
        mat.assign(1, 2, u.y);

        mat.assign(2, 0, f.z);
        mat.assign(2, 1, l.z);
        mat.assign(2, 2, u.z);
        return mat;
    }

    
    public static Matrix3x3 roofTo(Vector3 up, Vector3 generalDirection) {
        Vector3 f = new Vector3();

        if (generalDirection != null) {
            // https://stackoverflow.com/a/9605695

            double dist = generalDirection.dot(up);
            Vector3 projected = generalDirection.sub(up.mul(dist));
            f = projected.normalized();
        }

        if (f.isZero())
            f = new Vector3(0, 0, -1);
        Vector3 u = f.crossProduct(up.crossProduct(f)).normalized();
        Vector3 l = u.crossProduct(f).normalized();

        Matrix3x3 mat = new Matrix3x3();
        mat.assign(0, 0, f.x);
        mat.assign(0, 1, l.x);
        mat.assign(0, 2, u.x);

        mat.assign(1, 0, f.y);
        mat.assign(1, 1, l.y);
        mat.assign(1, 2, u.y);

        mat.assign(2, 0, f.z);
        mat.assign(2, 1, l.z);
        mat.assign(2, 2, u.z);
        return mat;
    }

    public float[][] getFloatMatrix() {
        return new float[][]{
                {get(0, 0), get(0, 1), get(0, 2)},
                {get(1, 0), get(1, 1), get(1, 2)},
                {get(2, 0), get(2, 1), get(2, 2)}
        };
    }

    public Vector3 toEuler() {
        return new Vector3(
                (float) Math.atan2(this.get(2, 0), new Vector2(this.get(0, 0), this.get(1, 0)).magnitude()),
                (float) Math.atan2(this.get(1, 0), this.get(0, 0)),
                (float) Math.atan2(-this.get(2, 1), this.get(2, 2))
        );
    }

    public Vector3 up() {
        return new Vector3(this.get(0, 2), this.get(1, 2), this.get(2, 2));
    }

    public Vector3 forward() {
        return new Vector3(this.get(0, 0), this.get(1, 0), this.get(2, 0));
    }

    public Vector3 right() {
        return new Vector3(this.get(0, 1), this.get(1, 1), this.get(2, 1));
    }

    public void assign(int row, int column, float value) {
        this.data[row + column * 3] = value;
    }

    public float get(int row, int column) {
        return this.data[row + column * 3];
    }

    public float det() {
        return
                +this.get(0, 0) * this.get(1, 1) * this.get(2, 2)
                        + this.get(0, 1) * this.get(1, 2) * this.get(2, 0)
                        + this.get(0, 2) * this.get(1, 0) * this.get(2, 1)
                        - this.get(0, 0) * this.get(1, 2) * this.get(2, 1)
                        - this.get(0, 1) * this.get(1, 0) * this.get(2, 2)
                        - this.get(0, 2) * this.get(1, 1) * this.get(2, 0);
    }

    public float tr() {
        float sum = 0;
        for (int i = 0; i < 3; i++)
            sum += get(i, i);
        return sum;
    }

    public Matrix3x3 invert() {
        Matrix3x3 invA = new Matrix3x3();

        float inv_detA = 1.0f / this.det();

        invA.assign(0, 0, (this.get(1, 1) * this.get(2, 2) - this.get(1, 2) * this.get(2, 1)) * inv_detA);
        invA.assign(0, 1, (this.get(0, 2) * this.get(2, 1) - this.get(0, 1) * this.get(2, 2)) * inv_detA);
        invA.assign(0, 2, (this.get(0, 1) * this.get(1, 2) - this.get(0, 2) * this.get(1, 1)) * inv_detA);
        invA.assign(1, 0, (this.get(1, 2) * this.get(2, 0) - this.get(1, 0) * this.get(2, 2)) * inv_detA);
        invA.assign(1, 1, (this.get(0, 0) * this.get(2, 2) - this.get(0, 2) * this.get(2, 0)) * inv_detA);
        invA.assign(1, 2, (this.get(0, 2) * this.get(1, 0) - this.get(0, 0) * this.get(1, 2)) * inv_detA);
        invA.assign(2, 0, (this.get(1, 0) * this.get(2, 1) - this.get(1, 1) * this.get(2, 0)) * inv_detA);
        invA.assign(2, 1, (this.get(0, 1) * this.get(2, 0) - this.get(0, 0) * this.get(2, 1)) * inv_detA);
        invA.assign(2, 2, (this.get(0, 0) * this.get(1, 1) - this.get(0, 1) * this.get(1, 0)) * inv_detA);

        return invA;
    }

    public Matrix3x3 div(float denominator) {
        Matrix3x3 B = new Matrix3x3();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                B.assign(i, j, this.get(i, j) / denominator);
            }
        }
        return B;
    }

    public Matrix3x3 elementwiseMul(float denominator) {
        Matrix3x3 B = new Matrix3x3();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                B.assign(i, j, this.get(i, j) * denominator);
            }
        }
        return B;
    }

    public Matrix3x3 add(Matrix3x3 other) {
        Matrix3x3 B = new Matrix3x3();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                B.assign(i, j, this.get(i, j) + other.get(i, j));
            }
        }
        return B;
    }

    public Matrix3x3 sub(Matrix3x3 other) {
        Matrix3x3 B = new Matrix3x3();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                B.assign(i, j, this.get(i, j) - other.get(i, j));
            }
        }
        return B;
    }

    public Matrix3x3 transpose() {
        Matrix3x3 other = new Matrix3x3();

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                other.assign(r, c, get(c, r));
            }
        }

        return other;
    }

    public Vector3 dot(Vector3 other) {
        float[] vecArr = new float[3];
        for (int i = 0; i < 3; i++) {
            vecArr[i] = 0;
            for (int j = 0; j < 3; j++) {
                vecArr[i] += get(i, j) * other.get(j);
            }
        }
        return new Vector3(vecArr[0], vecArr[1], vecArr[2]);
    }

    public Matrix3x3 matrixMul(Matrix3x3 other) {
        Matrix3x3 C = new Matrix3x3();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                C.assign(i, j, 0.0f);
                for (int k = 0; k < 3; k++) {
                    C.assign(i, j, (C.get(i, j) + this.get(i, k) * other.get(k, j)));
                }
            }
        }

        return C;
    }

    public float angle( Matrix3x3 other) {
        return (float) Math.acos(0.5f * (this.matrixMul(other.transpose()).tr() - 1.0f));
    }

    @Override
    public String toString() {
        float[][] dat = getFloatMatrix();
        final StringBuilder sb = new StringBuilder("Matrix3x3[\n");
        sb.append(Arrays.toString(dat[0])).append(",\n");
        sb.append(Arrays.toString(dat[1])).append(",\n");
        sb.append(Arrays.toString(dat[2])).append("\n");
        sb.append(']');
        return sb.toString();
    }
}
