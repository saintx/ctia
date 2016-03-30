(ns ctia.stores.atom.state
  (:import java.util.UUID)
  (:require [alandipert.enduro :as e]
            [clojure.string :as s]
            [ctia.properties :refer [properties]]
            [clojure.java.io :refer [delete-file file]]))

(def atom-config (get-in @properties [:ctia :store :atom]))

(defn model->file-path [model test?]
  (let [data-dir (if test?
                   (:testpath atom-config)
                   (:filepath atom-config))]
    (str data-dir "/" model)))

(defn file-atom [model test?]
  (println "making a file atom")
  (let [result
        (e/file-atom {}
                     (model->file-path model test?))]
    (println (model->file-path model test?))
    result))

(defn store-instance->name [store]
  (-> store
      type
      str
      (s/replace #"class" "")
      (s/split #"\$")
      first
      s/trim
      (s/split #"\.")
      last))

(defn init-atom! [store test?]
  (condp = (:persistence atom-config)
    "file" (file-atom (store-instance->name store) test?)
    (e/mem-atom {})))
