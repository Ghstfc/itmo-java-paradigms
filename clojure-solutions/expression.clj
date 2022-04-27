;./TestClojure.sh cljtest/linear/LinearTest easy Cuboid
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







