;./TestClojure.sh cljtest/linear/LinearTest easy base
(defn v+ [v1 v2] (mapv + v1 v2))
(defn v- [v1 v2] (mapv - v1 v2))
(defn v* [v1 v2] (mapv * v1 v2))
(defn vd [v1 v2] (mapv / v1 v2))
(defn scalar [v1 v2] (apply + (v* v1 v2)))
(defn vect [v1 v2] [(- (* (v1 1) (v2 2)) (* (v1 2) (v2 1)))
                    (- (- (* (v1 0) (v2 2)) (* (v1 2) (v2 0))))
                    (- (* (v1 0) (v2 1)) (* (v1 1) (v2 0)))])
(defn v*s [v1 v2] (vec (for [x v1] (* x v2))))
(defn m+ [v1 v2] (mapv v+ v1 v2))
(defn m- [v1 v2] (mapv v- v1 v2))
(defn m* [v1 v2] (mapv v* v1 v2))
(defn md [v1 v2] (mapv vd v1 v2))
(defn m*s [v1 v2] (vec (for [x v1] (v*s x v2))))
(defn m*v [v1 v2] (vec (for [x v1] (scalar x v2))))
(defn transpose [v] (vec (apply map vector v)))
(defn m*m [v1 v2]  (vec(transpose(vec(for [y (transpose v2)] (m*v v1 y))))))
(defn c+ [v1 v2]  (mapv m+ v1 v2))
(defn c- [v1 v2]  (mapv m- v1 v2))
(defn c* [v1 v2]  (mapv m* v1 v2))
(defn cd [v1 v2]  (mapv md v1 v2))








