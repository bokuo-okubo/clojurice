;;;; config.clj - app.config
;;; Contains the fetcher for the configuration files

(ns app.config
  (:require [schema.core :as s]
            [app.domain :as d]
            [puppetlabs.kitchensink.core :refer [deep-merge]]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn get-resource-file
  "Given a filename, loads it from config/ on the resource path
  and parses it as EDN into a Clojure map"
  [filename]
  (->> (str "config/" filename)
       io/resource
       slurp
       edn/read-string))

(defn get-config
  "Given a name for the desired profile config, 
  loads it from matching edn and base.edn and merges.
  Profile always takes precedence over base. The resulting config
  is then validated against the Config schema to ensure correct values."
  [profile-name]
  (let [base (get-resource-file "base.edn")
        prof (get-resource-file (str profile-name ".edn"))]
    (s/validate d/Config (deep-merge base prof))))
