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

(defn stores []
  {:actor [store/actor-store actor/->ActorStore]
   :campaign [store/campaign-store campaign/->CampaignStore]
   :coa [store/coa-store coa/->COAStore]
   :exploit [store/exploit-target-store target/->ExploitTargetStore]
   :feedback [store/feedback-store feedback/->FeedbackStore]
   :identity [store/identity-store identity/->IdentityStore]
   :indicator [store/indicator-store indicator/->IndicatorStore]
   :judgement [store/judgement-store judgement/->JudgementStore]
   :sighting [store/sighting-store sighting/->SightingStore]
   :ttp [store/ttp-store ttp/->TTPStore]})

(defn init-atom-store! []
  (doseq [[key vals] (stores)]
    (let [[store impl-fn] vals]
      (reset! store (impl-fn (e/file-atom {} (str "/tmp/ctia/" (name key))))))))

(comment (init-atom! actor/->ActorStore false)
         (println store/actor-store))
