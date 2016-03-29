(ns ctia.init
  (:require [clojure.edn :as edn]
            [ctia.auth :as auth]
            [ctia.auth.allow-all :as allow-all]
            [ctia.auth.threatgrid :as threatgrid]
            [ctia.properties :as properties]
            [ctia.store :as store]
            [ctia.stores.es.store :as es]
            [ctia.stores.es.index :as es-index]
            [ctia.stores.atom :refer [init-atom-store!]]))

(defn config []
  (edn/read-string (slurp "config.edn")))

(defn init-auth-service! []
  (let [auth-service-name (get-in @properties/properties [:auth :service])]
    (case auth-service-name
      "allow-all" (reset! auth/auth-service (allow-all/->AuthService))
      "threatgrid" (reset! auth/auth-service (threatgrid/make-auth-service
                                              (threatgrid/make-whoami-service)))
      (throw (ex-info "Auth service not configured"
                      {:message "Unknown service"
                       :requested-service auth-service-name})))))

(defn init-es-store! []
  (let [store-state (es-index/init-conn)
        store-impls {store/actor-store es/->ActorStore
                     store/judgement-store es/->JudgementStore
                     store/feedback-store es/->FeedbackStore
                     store/campaign-store es/->CampaignStore
                     store/coa-store es/->COAStore
                     store/exploit-target-store es/->ExploitTargetStore
                     store/incident-store es/->IncidentStore
                     store/indicator-store es/->IndicatorStore
                     store/ttp-store es/->TTPStore
                     store/sighting-store es/->SightingStore
                     store/identity-store es/->IdentityStore}]

    (es-index/create! (:conn store-state)
                      (:index store-state))

    (doseq [[store impl-fn] store-impls]
      (reset! store (impl-fn store-state)))))

(defn init! []
  (properties/init!)
  (init-auth-service!)
  (init-atom-store!))
