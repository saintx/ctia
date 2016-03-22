(ns ctia.init-mem-store
  (:require [ctia.store :as store]
            [ctia.stores.memory.actor :as actor]
            [ctia.stores.memory.campaign :as campaign]
            [ctia.stores.memory.coa :as coa]
            [ctia.stores.memory.exploit-target :as target]
            [ctia.stores.memory.feedback :as feedback]
            [ctia.stores.memory.identity :as identity]
            [ctia.stores.memory.incident :as incident]
            [ctia.stores.memory.indicator :as indicator]
            [ctia.stores.memory.judgement :as judgement]
            [ctia.stores.memory.sighting :as sighting]
            [ctia.stores.memory.ttp :as ttp]))

(defn init-mem-store! []
  (let [store-impls {store/actor-store     actor/->ActorStore
                     store/campaign-store  campaign/->CampaignStore
                     store/coa-store       coa/->COAStore
                     store/exploit-target-store target/->ExploitTargetStore
                     store/feedback-store  feedback/->FeedbackStore
                     store/identity-store  identity/->IdentityStore
                     store/indicator-store indicator/->IndicatorStore
                     store/judgement-store judgement/->JudgementStore
                     store/sighting-store  sighting/->SightingStore
                     store/ttp-store       ttp/->TTPStore}]
    (doseq [[store impl-fn] store-impls]
      (reset! store (impl-fn (atom {}))))))
