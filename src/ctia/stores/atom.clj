(ns ctia.stores.atom
  (:require [ctia.properties :as properties]
            [ctia.store :as store]
            [ctia.stores.atom.state :refer [init-atom!]]
            [ctia.stores.atom.actor :as actor]
            [ctia.stores.atom.campaign :as campaign]
            [ctia.stores.atom.coa :as coa]
            [ctia.stores.atom.exploit-target :as target]
            [ctia.stores.atom.feedback :as feedback]
            [ctia.stores.atom.identity :as identity]
            [ctia.stores.atom.incident :as incident]
            [ctia.stores.atom.indicator :as indicator]
            [ctia.stores.atom.judgement :as judgement]
            [ctia.stores.atom.sighting :as sighting]
            [ctia.stores.atom.ttp :as ttp]
            [alandipert.enduro :as e]))

(def config (get-in @properties/properties [:ctia :store :atom]))

(defn init-store-state [f]
  (fn []
    (f (init-atom! f true))))

(defn stores []
  {store/actor-store actor/->ActorStore
   store/campaign-store campaign/->CampaignStore
   store/coa-store coa/->COAStore
   store/exploit-target-store target/->ExploitTargetStore
   store/feedback-store feedback/->FeedbackStore
   store/identity-store identity/->IdentityStore
   store/indicator-store indicator/->IndicatorStore
   store/judgement-store judgement/->JudgementStore
   store/sighting-store sighting/->SightingStore
   store/ttp-store ttp/->TTPStore})

(defn init-atom-store! []
  (let [store-impls (stores)]
    (doseq [[store impl-fn] store-impls]
      (reset! store (impl-fn (init-atom! impl-fn false))))))
