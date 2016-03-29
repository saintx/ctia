(ns ctia.stores.atom.state
  (:import java.util.UUID)
  (:require [alandipert.enduro :as e]
            [clojure.string :as s]
            [ctia.properties :refer [properties]]
            [clojure.java.io :refer [delete-file file]]))

(def atom-config (get-in @properties [:ctia :store :atom]))

(def file-data-dir (get-in atom-config [:filepath]))

(defn test-file-data-dir []
  (str (get-in atom-config [:testpath])
       "/"
       (UUID/randomUUID)))

(defn purge-file-atoms! []
  (let [dir (file file-data-dir)]
    (println  (.listFiles dir))
    (when (.isDirectory dir)
      (dorun
       (map #(delete-file %) (.listFiles dir))))))

(defn model->file-path [model test?]
  (let [data-dir (if test?
                   (test-file-data-dir)
                   file-data-dir)]
    (str data-dir "/" model ".clj")))

(defn file-atom [model test?]
  (e/file-atom {}
               (model->file-path model test?)))

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
