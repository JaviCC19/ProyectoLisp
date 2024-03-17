(defun metodo-complejo ()
  (list 'if (> 5 3)
        (quote
          (progn
            (setq x 10)
            (+ x 5)))
        (quote
          (progn
            (setq x 5)
            (- x 2)))))

