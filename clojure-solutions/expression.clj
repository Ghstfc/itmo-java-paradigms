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
(def _perm (field :perm))
(def _sign (field :sign))
(def evaluate (method :_evaluate))
(def toString (method :_toString))
(def toStringSuffix (method :_toSuffix))

(defn Var [this v]
  (assoc this :v (clojure.string/lower-case (str (get v 0))) :perm v :sign "cnst"))
(defn Cnst [this v]
  (assoc this :v v :sign "cnst" :perm v))
(defn Neg [this v]
  (assoc this :v v :sign "negate"))
(defn _Ln [this v]
  (assoc this :v v :sign "ln"))


(def m1 {"negate" 0 "cnst" 1})

(def UnoProto
  {
   :_evaluate (fn [x vars]
                (cond
                  (contains? vars (_v x)) (get vars (_v x))
                  (= "negate" (_sign x)) (- (evaluate (_v x) vars))
                  (= "ln" (_sign x)) (Math/log (Math/abs (evaluate (_v x) vars)))
                  :else (double (_v x))
                  ))
   :_toString (fn [expr]
                (cond
                  (= "cnst" (_sign expr)) (str (_v expr))
                  :else
                  (str "(" (_sign expr) " " (toString (_v expr)) ")")
                  ))
   :_toSuffix (fn [expr]
                (cond
                  (= "cnst" (_sign expr)) (str (_perm expr))
                  :else
                  (str "(" (toStringSuffix (_v expr)) " " (_sign expr) ")")
                  ))
   })

(def Constant (constructor Cnst UnoProto))
(def Variable (constructor Var UnoProto))
(def Negate (constructor Neg UnoProto))
(def Ln (constructor _Ln UnoProto))

(defn Bin [this v1 v2]
  (assoc this :v1 v1 :v2 v2))

(defn My_Pow [e1 e2] (Math/pow e1 e2))

(def m {"+" + "-" - "*" * "pow" My_Pow})

(def BinProto
  {
   :_evaluate (fn [x vars] (cond
                             (= "/" (_sign x)) (/ (double (evaluate (_v1 x) vars)) (double (evaluate (_v2 x) vars)))
                             (= "log" (_sign x)) (/ (Math/log (Math/abs (evaluate (_v2 x) vars))) (Math/log (Math/abs (evaluate (_v1 x) vars))))
                             :else
                             ((get m (_sign x)) (evaluate (_v1 x) vars) (evaluate (_v2 x) vars))
                             ))
   :_toString (fn [x] (str "(" (_sign x) " " (toString (_v1 x)) " " (toString (_v2 x)) ")"))
   :_toSuffix (fn [x] (str "(" (toStringSuffix (_v1 x)) " " (toStringSuffix (_v2 x)) " " (_sign x) ")"))
   })

(defn _Add [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "+"))
(defn _Subtract [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "-"))
(defn _Divide [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "/"))
(defn _Multiply [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "*"))
(defn _Pow [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "pow"))
(defn _Log [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "log"))

(def Add (constructor _Add BinProto))
(def Divide (constructor _Divide BinProto))
(def Subtract (constructor _Subtract BinProto))
(def Multiply (constructor _Multiply BinProto))
(def Log (constructor _Log BinProto))
(def Pow (constructor _Pow BinProto))

(defn diff [x vars]
  (cond
    (= "+" (_sign x)) (Add (diff (_v1 x) vars) (diff (_v2 x) vars))
    (= "-" (_sign x)) (Subtract (diff (_v1 x) vars) (diff (_v2 x) vars))
    (= "*" (_sign x)) (Add (Multiply (diff (_v1 x) vars) (_v2 x))
                           (Multiply (_v1 x) (diff (_v2 x) vars)))
    (= "/" (_sign x)) (Divide (Subtract (Multiply (diff (_v1 x) vars) (_v2 x))
                                        (Multiply (_v1 x) (diff (_v2 x) vars))) (Multiply (_v2 x) (_v2 x)))
    (= "pow" (_sign x)) (Multiply (Pow (_v1 x) (_v2 x)) (diff (Multiply (_v2 x) (Ln (_v1 x))) vars))
    (= "ln" (_sign x)) (Multiply (Divide (Constant 1) (_v x)) (diff (_v x) vars))
    (= "log" (_sign x)) (diff (Divide (Ln (_v2 x)) (Ln (_v1 x))) vars)

    (= vars (_v x)) (Constant 1)
    (= "cnst" (_sign x)) (Constant 0)
    (= "negate" (_sign x)) (Negate (diff (_v x) vars))
    :else (Constant 0)
    ))

(def m-b {'+ Add '- Subtract '* Multiply '/ Divide 'log Log 'pow Pow})
(def m-u {'negate Negate})

(defn parsef [s]
  (cond
    (number? s) (Constant s)
    (symbol? s) (Variable (name s))
    (= 2 (count s)) ((get m-u (nth s 0)) (parsef (nth s 1)))
    :else ((get m-b (nth s 0)) (parsef (nth s 1)) (parsef (nth s 2)))
    ))

(defn parseObject [s] (parsef (read-string s)))


;hw 12
;(load-file "parser.clj")

;(def ops {'+ Add '- Subtract '* Multiply '/ Divide 'log Log 'pow Pow 'negate Negate})
;(def *digit (+char "0123456789"))
;(def *number (+map read-string (+str (+plus *digit))))
;
;(def *space (+char " \t\n\r"))
;(def *ws (+ignore (+star *space)))
;
;(def *var (+map (comp Variable str) (+char "xyz")))
;
;(def *sub (+map (constantly Subtract) (_char "-")))
;(def *add (+map (constantly Add) (_char "+")))
;(def *div (+map (constantly Divide) (_char "/")))
;(def *mul (+map (constantly Multiply) (_char "*")))
;(def *neg (+map (constantly Negate) (+seq (_char "n") (_char "e") (_char "g") (_char "a") (_char "t") (_char "e"))))
;(def *pow (+map (constantly Pow) (+seq (_char "p") (_char "o") (_char "w"))))
;(def *pow (+map (constantly Log) (+seq (_char "l") (_char "o") (_char "g"))))
; (+seqf (fn [ps] apply (last ps) ps ) *( *value *ws (+opt *value) *ws *f *))
;(declare *suffix)
;(def *list (+map ()))


(defn parsing [s]
  (cond
    (number? s) (Constant s)
    (symbol? s) (Variable (name s))
    (= 2 (count s)) ((get m-u (nth s 1)) (parsing (nth s 0)))
    :else ((get m-b (nth s 2)) (parsing (nth s 0)) (parsing (nth s 1)))
    ))

(defn parseObjectSuffix [s] (parsing (read-string s)))



