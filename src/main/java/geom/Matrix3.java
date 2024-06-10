
package geom;

public class Matrix3 {
  private double[][] _data = new double[3][3];

  public Matrix3(double a11, double a12, double a13, double a21, double a22, double a23, double a31, double a32, double a33) {
    _data[0][0] = a11;
    _data[0][1] = a12;
    _data[0][2] = a13;
    _data[1][0] = a21;
    _data[1][1] = a22;
    _data[1][2] = a23;
    _data[2][0] = a31;
    _data[2][1] = a32;
    _data[2][2] = a33;
  }

  /**
   * The identity matrix.
   */
  public Matrix3() {
    _data[0][0] = 1;
    _data[0][1] = 0;
    _data[0][2] = 0;
    _data[1][0] = 0;
    _data[1][1] = 1;
    _data[1][2] = 0;
    _data[2][0] = 0;
    _data[2][1] = 0;
    _data[2][2] = 1;
  }

  public Matrix3(double[][] mat) {
    for (int i = 0; i < 3; i++){
      for (int j = 0; j < 3; j++){
        _data[i][j] = mat[i][j];
      }
    }
  }

  /**
   * Find solution of the system using Kramer's method.
   *
   * @param b1 free term of the 1st equation
   * @param b2 free term of the 2nd equation
   * @param b3 free term of the 3rd equation
   * @return
   * @throws geom.ExZeroDivision if system is degenerate
   */
  public Vect3d solve(double b1, double b2, double b3) throws ExZeroDivision {
    Vect3d v = new Vect3d();
    double d = Det23.calc(
        get(0,0), get(0,1), get(0,2),
        get(1,0), get(1,1), get(1,2),
        get(2,0), get(2,1), get(2,2));
    if (d == 0) {
      throw new ExZeroDivision("matrix is degenerate");
    }
    double d1 = Det23.calc(
        b1, get(0,1), get(0,2),
        b2, get(1,1), get(1,2),
        b3, get(2,1), get(2,2));
    double d2 = Det23.calc(
        get(0,0), b1, get(0,2),
        get(1,0), b2, get(1,2),
        get(2,0), b3, get(2,2));
    double d3 = Det23.calc(
        get(0,0), get(0,1), b1,
        get(1,0), get(1,1), b2,
        get(2,0), get(2,1), b3);
    v.set_x(d1 / d);
    v.set_y(d2 / d);
    v.set_z(d3 / d);
    return v;
  }

  public double get(int i, int j){
    return _data[i][j];
  }

  public Vect3d mulOnVect(Vect3d v) {
    double x = _data[0][0] * v.x()+ _data[0][1] * v.y()+ _data[0][2] * v.z();
    double y = _data[1][0] * v.x()+ _data[1][1] * v.y()+ _data[1][2] * v.z();
    double z = _data[2][0] * v.x()+ _data[2][1] * v.y()+ _data[2][2] * v.z();
    Vect3d _v = new Vect3d(x,y,z);
    return _v;
  }

  /**
   * Add a matrix to the original matrix
   * @param mat Matrix to add
   * @return this instance (for chain call).
   */
  public Matrix3 add(Matrix3 mat){
    for (int i = 0; i < 3; i++){
      for (int j = 0; j < 3; j++){
        _data[i][j] += mat.get(i, j);
      }
    }
    return this;
  }

  /**
   * Multiply the matrix to the number
   * @param num Number to multiply
   * @return this instance (for chain call).
   */
  public Matrix3 multiply(double num){
    for (int i = 0; i < 3; i++){
      for (int j = 0; j < 3; j++){
        _data[i][j] *= num;
      }
    }
    return this;
  }
  
  /**
   * Multiply the matrix to the matrix
   * @param m second
   * @return this instance (for chain call).
   */
  public Matrix3 rightMultiply(Matrix3 m){
    _data = multiply(this, m).data();
    return this;
  }
  
    /**
   * Multiply the matrix to the matrix
   * @param m second
   * @return this instance (for chain call).
   */
  public Matrix3 leftMultiply(Matrix3 m){
    _data = multiply(m, this).data();
    return this;
  }
  
  public static Matrix3 multiply(Matrix3 m1, Matrix3 m2) {
    double[][] data = new double[3][3];
    for (int i = 0; i < 3; i++){
      for (int j = 0; j < 3; j++){
        data[i][j] = m1.get(i, 0) * m2.get(0, j) +
                     m1.get(i, 1) * m2.get(1, j) +
                     m1.get(i, 2) * m2.get(2, j);
      }
    }
    return new Matrix3(data);
  }

  /**
   * Sum of two matrices
   * @param mat1 First matrix
   * @param mat2 Second matrix
   * @return Sum of two matrices ( mat1 + mat2 )
   */
  static public Matrix3 sum(Matrix3 mat1, Matrix3 mat2){
    double[][] res = new double[3][3];
    for (int i = 0; i < 3; i++){
      for (int j = 0; j < 3; j++){
        res[i][j] = mat1.get(i, j) + mat2.get(i, j);
      }
    }
    return new Matrix3(res);
  }

  /**
   * Product of matrix and number
   * @param mat Matrix to multiply
   * @param num Number to multiply
   * @return
   */
  static public Matrix3 multiply(Matrix3 mat, double num){
    double[][] res = new double[3][3];
    for (int i = 0; i < 3; i++){
      for (int j = 0; j < 3; j++){
        res[i][j] = mat.get(i, j) * num;
      }
    }
    return new Matrix3(res);
  }

  public double[] getAsArray(){
    double[] res = new double[9];
    for (int i = 0; i < 3; i++){
      for (int j = 0; j < 3; j++){
        res[ i * 3 + j ] = _data[i][j];
      }
    }
    return res;
  }
  
  public double[][] data() {
    return _data;
  }

  /**
   * Calculate the rotation matrix, which produce a rotation by angle around the vector.
   * To rotate the vector by the matrix, use the mulOnVect function.
   * Similar matrix is generated by a glRotate* function call.
   * @param angle Specifies the angle of rotation, in radians.
   * @param x The x coordinate of a vector.
   * @param y The y coordinate of a vector.
   * @param z The z coordinate of a vector.
   * @return Rotation matrix
   */
  static public Matrix3 getRotateMatrix(double angle, double x, double y, double z){
    Vect3d v = new Vect3d(x, y, z);
    v = Vect3d.getNormalizedVector(v);
    Matrix3 mat1 = new Matrix3(
            v.x()*v.x(), v.x()*v.y(), v.x()*v.z(),
            v.y()*v.x(), v.y()*v.y(), v.y()*v.z(),
            v.z()*v.x(), v.z()*v.y(), v.z()*v.z());
    Matrix3 mat2 = new Matrix3(
            1 - v.x()*v.x(), - v.x()*v.y(), - v.x()*v.z(),
            - v.y()*v.x(), 1 - v.y()*v.y(), - v.y()*v.z(),
            - v.z()*v.x(), - v.z()*v.y(), 1 - v.z()*v.z());
    Matrix3 mat3 = new Matrix3(
            0, -v.z(), v.y(),
            v.z(), 0, - v.x(),
            - v.y(), v.x(), 0);
    mat2.multiply(Math.cos(angle));
    mat3.multiply(Math.sin(angle));
    Matrix3 mat = Matrix3.sum(Matrix3.sum(mat1, mat2), mat3);

    return mat;
  }

  /**
   * Calculate the rotation matrix, which produce a rotation by angle around the vector.
   * To rotate the vector by the matrix, use the mulOnVect function.
   * Similar matrix is generated by a glRotate* function call.
   * @param angle Specifies the angle of rotation, in radians.
   * @param normal Vector around which the rotation takes place
   * @return
   */
  static public Matrix3 getRotateMatrix(double angle, Vect3d normal){
    return getRotateMatrix(angle, normal.x(), normal.y(), normal.z());
  }
}
