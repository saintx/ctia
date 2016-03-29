(ns ctia.test-helpers.atom
  "Atom Store test helpers"
  (:require [ctia.store :as store]
            [ctia.stores.atom.state :refer [init-atom!]]
            [ctia.test-helpers.core :as h]
            [ctia.properties :as properties]
            [ctia.stores.atom.actor :as ma]
            [ctia.stores.atom.campaign :as mca]
            [ctia.stores.atom.coa :as mco]
            [ctia.stores.atom.exploit-target :as me]
            [ctia.stores.atom.feedback :as mf]
            [ctia.stores.atom.identity :as mi]
            [ctia.stores.atom.incident :as mic]
            [ctia.stores.atom.indicator :as min]
            [ctia.stores.atom.judgement :as mj]
            [ctia.stores.atom.sighting :as ms]
            [ctia.stores.atom.ttp :as mt]))

(defn init-store-state [f]
  (fn []
    (f (init-atom! f true))))

(def atom-stores
  {store/actor-store          (init-store-state ma/->ActorStore)
   store/judgement-store      (init-store-state mj/->JudgementStore)
   store/feedback-store       (init-store-state mf/->FeedbackStore)
   store/campaign-store       (init-store-state mca/->CampaignStore)
   store/coa-store            (init-store-state mco/->COAStore)
   store/exploit-target-store (init-store-state me/->ExploitTargetStore)
   store/incident-store       (init-store-state mic/->IncidentStore)
   store/indicator-store      (init-store-state min/->IndicatorStore)
   store/sighting-store       (init-store-state ms/->SightingStore)
   store/ttp-store            (init-store-state mt/->TTPStore)
   store/identity-store       (init-store-state mi/->IdentityStore)})

(def fixture-atom-store
  (do (properties/init!)
            (h/fixture-store atom-stores)))
