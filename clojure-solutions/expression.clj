; hw 10
(defn add [e1 e2] (fn [x] (+ (e1 x) (e2 x))))
(defn subtract [e1 e2] (fn [x] (- (e1 x) (e2 x))))
(defn multiply [e1 e2] (fn [x] (* (e1 x) (e2 x))))
(defn divide [e1 e2] (fn [x] (/ (double (e1 x)) (double (e2 x)))))
(defn variable [e] (fn [x] (get x e)))
(defn constant [e] (fn [x] e))
(defn negate [e] (fn [x] (- (e x))))
(defn pow [e1 e2] (fn [x] (Math/pow (e1 x) (e2 x))))
(defn log [e1 e2] (fn [x] (/ (Math/log (Math/abs (e2 x))) (Math/log (Math/abs (e1 x))))))
(def bin {'+ add '- subtract '* multiply '/ divide 'negate negate 'pow pow 'log log})
(def uno {'negate negate})
(defn parse [s]
  (cond
    (number? s) (constant s)
    (symbol? s) (variable (name s))
    (= 2 (count s)) ((get uno (nth s 0)) (parse (nth s 1)))
    :else ((get bin (nth s 0)) (parse (nth s 1)) (parse (nth s 2)))
    ))
(defn parseFunction [s] (parse (read-string s)))

; hw 11 (bcs same name of file)

(defn proto-get
  ([obj key] (proto-get obj key nil))
  ([obj key default]
   (cond
     (contains? obj key) (obj key)
     (contains? obj :prototype) (proto-get (obj :prototype) key default)
     :else default)))

(defn proto-call
  [this key & args]
  (apply (proto-get this key) this args))

(defn field [key]
  (fn
    ([this] (proto-get this key))
    ([this def] (proto-get this key def))))

(defn method
  [key] (fn [this & args] (apply proto-call this key args)))

(defn constructor
  [ctor prototype]
  (fn [& args] (apply ctor {:prototype prototype} args)))

(def _v1 (field :v1))
(def _v2 (field :v2))
(def _v (field :v))
(def _sign (field :sign))
(def evaluate (method :_evaluate))
(def toString (method :_toString))

(defn Cnst [this v]
  (assoc this :v v :sign "%"))
(defn Neg [this v]
  (assoc this :v v :sign "$"))

(def m1 {"$" 0 "%" 1})

(def UnoProto
  {
   :_evaluate (fn [x vars]
                (cond
                  (contains? vars (_v x)) (double (get vars (_v x)))
                  (= 0 (get m1 (_sign x))) (double (- (evaluate (_v x) vars)))
                  :else (double (_v x))
                  ))
   :_toString (fn [expr]
                (cond
                 (= "$" (_sign expr)) (str "(negate " (toString (_v expr)) ")")
                 :else
                 (str (_v expr))
                 ))
   })

(def Constant (constructor Cnst UnoProto))
(def Variable (constructor Cnst UnoProto))
(def Negate (constructor Neg UnoProto))

(defn Bin [this v1 v2]
  (assoc this :v1 v1 :v2 v2))

(def m {"+" + "-" - "/" / "*" *})

(def BinProto
  {
   :_evaluate (fn [x vars] (cond
                             (= "/" (_sign x)) (/ (double (evaluate (_v1 x) vars)) (double (evaluate (_v2 x) vars)))
                             (= "*" (_sign x)) (* (evaluate (_v1 x) vars) (evaluate (_v2 x) vars))
                             (= "+" (_sign x)) (+ (evaluate (_v1 x) vars) (evaluate (_v2 x) vars))
                             (= "-" (_sign x)) (- (evaluate (_v1 x) vars) (evaluate (_v2 x) vars))
                             :else (evaluate (_v x) vars)
   ; у меня сгорела жопа, ибо решение ниже должно работать идеально, но из-за незнания того,
   ; что находится под капотом clojure'a, я не смог это сделать, ибо при делении даблов все равно выдавало ошибку (деление на 0)
   ; скорее всего это связано с тем, что при доставании слэша из мапы все аргументы кастуются к object, поэтому
   ; при делении просиходит деление object на object и кидается ошибка, но это не точно (решение снизу все равно классное)
                             ;((get m (_sign x)) (double (evaluate (_v1 x) vars)) (double (evaluate (_v2 x) vars)))
                             ))
   :_toString (fn [x] (str "(" (_sign x) " " (toString (_v1 x)) " " (toString (_v2 x)) ")"))
   })

(defn _Add [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "+"))
(defn _Subtract [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "-"))
(defn _Divide [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "/"))
(defn _Multiply [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "*"))

(def Add (constructor _Add BinProto))
(def Divide (constructor _Divide BinProto))
(def Subtract (constructor _Subtract BinProto))
(def Multiply (constructor _Multiply BinProto))

(defn diff [x vars]
  (cond
    (= "+" (_sign x)) (Add (diff (_v1 x) vars) (diff (_v2 x) vars))
    (= "-" (_sign x)) (Subtract (diff (_v1 x) vars) (diff (_v2 x) vars))
    (= "*" (_sign x)) (Add (Multiply (diff (_v1 x) vars) (_v2 x))
                           (Multiply (_v1 x) (diff (_v2 x) vars)))
    (= "/" (_sign x)) (Divide (Subtract (Multiply (diff (_v1 x) vars) (_v2 x))
                           (Multiply (_v1 x) (diff (_v2 x) vars))) (Multiply (_v2 x) (_v2 x)))
    (= vars (_v x)) (Constant 1)
    (= "%" (_sign x)) (Constant 0)
    (= "$" (_sign x)) (Negate (diff (_v x) vars))
    :else (Constant 0)
    ))

(def m-b {'+ Add '- Subtract '* Multiply '/ Divide})
(def m-u {'negate Negate})

(defn parsef [s]
  (cond
    (number? s) (Constant s)
    (symbol? s) (Variable (name s))
    (= 2 (count s)) ((get m-u (nth s 0)) (parsef (nth s 1)))
    :else ((get m-b (nth s 0)) (parsef (nth s 1)) (parsef (nth s 2)))
    ))

(defn parseObject [s] (parsef (read-string s)))




