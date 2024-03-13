(defun mi-condicional (x)
  (cond
    ((= x 0) "X es igual a cero")
    ((> x 0) "X es mayor que cero")
    ((< x 0) "X es menor que cero")
    (t "X no es un número entero")))
