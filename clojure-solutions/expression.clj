;./TestClojure.sh cljtest/linear/LinearTest easy Cuboid
(defn add [e1 e2] (fn [x] (+ (e1 x) (e2 x))))
(defn subtract [e1 e2] (fn [x] (- (e1 x) (e2 x))))
(defn multiply [e1 e2] (fn [x] (* (e1 x) (e2 x))))
(defn divide [e1 e2] (fn [x] (/ (double (e1 x)) (double (e2 x)))))
(defn variable [e] (fn [x] (get x e)))
(defn constant [e] (fn [x] e))
(defn negate [e] (fn [x] (- (e x))))
(defn sinh [e] (fn [x] (Math/sinh (e x))))
(defn cosh [e] (fn [x] (Math/cosh (e x))))



(def bin {'+ add '- subtract '* multiply '/ divide 'negate negate})
(def uno {'sinh sinh 'cosh cosh 'negate negate})

(defn parse [s]
  (cond
    (number? s) (constant s)
    (symbol? s) (variable (name s))
    (= 2 (count s)) ((get uno (nth s 0)) (parse (nth s 1)))
    :else ((get bin (nth s 0)) (parse (nth s 1)) (parse (nth s 2)))
    ))




(defn parseFunction [s] (parse (read-string s)))







