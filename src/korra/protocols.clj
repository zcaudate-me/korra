(ns korra.protocols)

(defmulti resolve-coordinates (fn [t & [k v]] k))

(defmulti resolve-jar (fn [t & [k v]] k))


(comment
  (def resolve-coordinates nil)
  (def resolve-jar nil))
