(ns ctia.stores.atom.transformation
  (:require [ctia.lib.specter.paths :as path]
            [clj-time.coerce :as coerce]
            [com.rpl.specter :refer :all]))

(defn javadates-to-datetimes
  "walk entities and convert Java Date objects to joda DateTimes"
  [entities]
  (transform path/walk-javadates
             coerce/from-date
             entities))

(comment
  [{:0 0, :2 {:1 1, :3 {:4 #inst "2016-03-28T17:26:04.453-00:00", :5 {:6 #inst "2016-03-28T17:26:04.453-00:00"}}}}])

(defn datetimes-to-javadates
  "walk entities and convert joda DateTimes to Java Date objects"
  [entities]
  (transform path/walk-datetimes
             coerce/to-date
             entities))
