(ns ctia.stores.atom.common
  (:require [ctia.schemas.common :as c]
            [schema.core :as s]
            [alandipert.enduro :as e]
            [ctia.stores.atom.transformation :as xform])
  (:import java.util.UUID))

(defn random-id [prefix]
  (fn [_new-entity_]
    (str prefix "-" (UUID/randomUUID))))

(defn make-swap-fn [entity-fn]
  (fn [state-map new-model & [id :as args]]
    (assoc state-map
           id
           (apply entity-fn
                  (xform/datetimes-to-javadates new-model)
                  args))))

(defn xform-get
  "transforming get"
  [m k]
  (xform/javadates-to-datetimes (get m k)))

(defmacro def-read-handler [name Model]
  `(s/defn ~name :- (s/maybe ~Model)
     [state# :- (s/protocol e/IDurableAtom)
      id# :- s/Str]
     (xform-get (deref state#) id#)))

(defmacro def-create-handler [name Model NewModel swap-fn id-fn]
  `(s/defn ~name :- ~Model
     [state# :- (s/protocol e/IDurableAtom)
      login# :- s/Str
      new-model# :- ~NewModel]
     (let [new-id# (~id-fn new-model#)]
       (xform-get (e/swap! state# ~swap-fn new-model# new-id# login#) new-id#))))

(defmacro def-update-handler [name Model NewModel swap-fn]
  `(s/defn ~name :- ~Model
     [state# :- (s/protocol e/IDurableAtom)
      id# :- c/ID
      login# :- s/Str
      updated-model# :- ~NewModel]
     (xform-get (e/swap! state#
                         ~swap-fn
                         updated-model#
                         id#
                         login#
                         (get (deref state#) id#))
                id#)))

(defmacro def-delete-handler [name Model]
  `(s/defn ~name :- s/Bool
     [state# :- (s/protocol e/IDurableAtom)
      id# :- s/Str]
     (if (contains? (deref state#) id#)
       (do (e/swap! state# dissoc id#)
           true)
       false)))

(defmacro def-list-handler [name Model]
  `(s/defn ~name :- (s/protocol e/IDurableAtom)
     [state# :- (s/protocol e/IDurableAtom)
      filter-map# :- {s/Any s/Any}]
     (xform/javadates-to-datetimes
      (into []
            (filter (fn [model#]
                      (every? (fn [[k# v#]]
                                (if (sequential? k#)
                                  (= v# (get-in model# k# ::not-found))
                                  (= v# (get model# k# ::not-found))))
                              filter-map#))
                    (vals (deref state#)))))))
