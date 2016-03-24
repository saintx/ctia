(ns ctia.init-atom-store
  (:require [ctia.store :as store]
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

(defn init-atom-store!
  [durable]
  (println "durable: " durable)
  (let [store-impls
        {:actor [store/actor-store actor/->ActorStore]
         :campaign [store/campaign-store campaign/->CampaignStore]
         :coa [store/coa-store coa/->COAStore]
         :exploit-target [store/exploit-target-store target/->ExploitTargetStore]
         :feedback [store/feedback-store feedback/->FeedbackStore]
         :identity [store/identity-store identity/->IdentityStore]
         :indicator [store/indicator-store indicator/->IndicatorStore]
         :judgement [store/judgement-store judgement/->JudgementStore]
         :sighting [store/sighting-store sighting/->SightingStore]
         :ttp [store/ttp-store ttp/->TTPStore]}]
    (doseq [[key values] store-impls]
      (let [[store impl-fn] values]
        (reset! store
                (impl-fn (if durable
                           (let [result (e/file-atom {}
                                                      (str "/tmp/ctia/" (name key))
                                                      :pending-dir "/tmp/ctia")]
                             (println result)
                             result)
                           (e/mem-atom {}))))))))
