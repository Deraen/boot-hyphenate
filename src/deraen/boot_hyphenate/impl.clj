(ns deraen.boot-hyphenate.impl
  (:require [clj-hyphenate.core :refer [hyphenate-paragraph]]
            [clj-hyphenate.patterns.en-gb :as en]
            [boot.util :as u]
            [clojure.java.io :as io]
            [net.cgrand.enlive-html :as html]
            [clojure.string :as string]))

(def hyphnate-els #{[:p] [:h1] [:h2] [:h3] [:h4] [:h5] [:h6] [:del] [:strong] [:em]})

(defn hyphenate-el [rules {:keys [content] :as el}]
  (assoc el :content (map (fn [x]
                            (if (string? x)
                              (hyphenate-paragraph rules x)
                              x))
                          content)))

(defn hyphenate-html' [rules file]
  (-> file
      (html/html-resource)
      (html/transform hyphnate-els (partial hyphenate-el rules))
      html/emit*
      (->> (apply str))))

(defn hyphenate-html [language in-path out-path]
  (require (symbol (str "clj-hyphenate.patterns." language)))
  (spit (io/file out-path)
        (hyphenate-html' @(resolve (symbol (format "clj-hyphenate.patterns.%s/rules" language))) (io/file in-path))))
