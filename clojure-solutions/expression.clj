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

;                 New Var and Const
;----------------------------------------------------
(def func (field :func))
(def diff (method :_diff))


(declare Constant)
(declare Variable)
(declare Add)
(declare Subtract)
(declare Multiply)
(declare Divide)
(declare Negate)
(declare Ln)
(declare Pow)

(def VarProt {
              :_evaluate (fn [x vars] (get vars (_v x)))
              :_toString (fn [expr] (str (_v expr)))
              :_toSuffix (fn [expr] (str (_perm expr)))
              })

(def ConstProto {
                 :_evaluate (fn [x vars] (_v x))
                 :_toString (fn [expr] (str (_v expr)))
                 :_toSuffix (fn [expr] (str (_v expr)))
                 })

(defn Var [this v]
  (assoc this :v (clojure.string/lower-case (str (get v 0))) :perm v
              :_diff (fn [x vars] (if (= vars (_v x)) (Constant 1) (Constant 0)))
              ))

; :NOTE: you allocate on each 0 (and 1) constant usage

(defn Cnst [this v]
  (assoc this :v v :perm v :_diff (fn [x vars] (Constant 0))
              ))

;(def Cnst
;  {:_diff (fn [x vars] (Constant 0))})


(def Constant (constructor Cnst ConstProto))
(def Variable (constructor Var VarProt))

;                     New Negate and Ln
;---------------------------------------------------------------

(defn Neg [this v]
  (assoc this :v v :func (fn [x] (- x)) :sign "negate"
              :_diff (fn [x vars] (Negate (diff (_v x) vars)))))
(defn _Ln [this v]
  (assoc this :v v :func (fn [x] (Math/log (Math/abs x))) :sign "ln"
              :_diff (fn [x vars] (Multiply (Divide (Constant 1) (_v x)) (diff (_v x) vars)))
              ))

(def UnoProto
  {
   :_evaluate (fn [x vars] ((func x) (evaluate (_v x) vars)))
   :_toString (fn [x] (str "(" (_sign x) " " (toString (_v x)) ")"))
   :_toSuffix (fn [x] (str "(" (toStringSuffix (_v x)) " " (_sign x) ")"))
   })

(def Negate (constructor Neg UnoProto))
(def Ln (constructor _Ln UnoProto))

;            New Add Subtract Divide Multiply
;---------------------------------------------------------------

(defn _Add [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "+" :func (fn [a b] (+ a b))
              :_diff (fn [x vars] (Add (diff (_v1 x) vars) (diff (_v2 x) vars)))
              ))
(defn _Subtract [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "-" :func (fn [a b] (- a b))
              :_diff (fn [x vars] (Subtract (diff (_v1 x) vars) (diff (_v2 x) vars)))
              ))
(defn _Divide [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "/" :func (fn [a b] (/ (double a) b))
              :_diff (fn [x vars] (Divide (Subtract (Multiply (diff (_v1 x) vars) (_v2 x))
                                                    (Multiply (_v1 x) (diff (_v2 x) vars))) (Multiply (_v2 x) (_v2 x))))
              ))
(defn _Multiply [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "*" :func (fn [a b] (* a b))
              :_diff (fn [x vars] (Add (Multiply (diff (_v1 x) vars) (_v2 x))
                                       (Multiply (_v1 x) (diff (_v2 x) vars))))
              ))
(defn _Pow [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "pow" :func (fn [a b] (Math/pow a b))
              :_diff (fn [x vars] (Multiply (Pow (_v1 x) (_v2 x)) (diff (Multiply (_v2 x) (Ln (_v1 x))) vars)))
              ))
(defn _Log [this v1 v2]
  (assoc this :v1 v1 :v2 v2 :sign "log" :func (fn [a b] (/ (Math/log (Math/abs b)) (Math/log (Math/abs a))))
              :_diff (fn [x vars] (diff (Divide (Ln (_v2 x)) (Ln (_v1 x))) vars))
              ))

(def BinProto
  {
   :_evaluate (fn [x vars] ((func x) (evaluate (_v1 x) vars) (evaluate (_v2 x) vars)))
   :_toString (fn [x] (str "(" (_sign x) " " (toString (_v1 x)) " " (toString (_v2 x)) ")"))
   :_toSuffix (fn [x] (str "(" (toStringSuffix (_v1 x)) " " (toStringSuffix (_v2 x)) " " (_sign x) ")"))
   })

(def Add (constructor _Add BinProto))
(def Divide (constructor _Divide BinProto))
(def Subtract (constructor _Subtract BinProto))
(def Multiply (constructor _Multiply BinProto))
(def Log (constructor _Log BinProto))
(def Pow (constructor _Pow BinProto))

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
(load-file "parser.clj")

(def *digit (+map #(apply str %) (+plus (+char "-0123456789"))))
(def *number (+map (comp Constant read-string) (+map #(apply str %) (+seq *digit (+opt (+map #(apply str %) (+seq (+char ".") *digit)))))))
(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))
(def *var (+map (comp Variable str) (+map #(apply str %) (+plus (+char "xyzXYZ")))))
(def *sub (+map (constantly Subtract) (+char "-")))
(def *add (+map (constantly Add) (+char "+")))
(def *div (+map (constantly Divide) (+char "/")))
(def *mul (+map (constantly Multiply) (+char "*")))
(def *neg (+map (constantly Negate) (+seq (+char "n") (+char "e") (+char "g") (+char "a") (+char "t") (+char "e"))))


(def *list (+map #(apply (last %) ((comp reverse rest reverse) %))
                 (+seq
                   (+ignore (+char "("))
                   *ws
                   (+or *number *var (delay *list))
                   *ws
                   (+or *number *var (delay *list) *ws)
                   *ws
                   (+or *sub *add *div *mul *neg)
                   *ws
                   (+ignore (+char ")")))))
(defn parseObjectSuffix [s] (-value ((+map first (+seq *ws (+or *list *var *number))) s)))


