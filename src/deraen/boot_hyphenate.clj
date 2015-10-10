(ns deraen.boot-hyphenate
  {:boot/export-tasks true}
  (:require [clojure.java.io :as io]
            [boot.pod        :as pod]
            [boot.core       :as b]
            [boot.util       :as util]))

(def ^:private deps
  '[[deraen/clj-hyphenate "0.1.0"]
     [enlive "1.1.6"]])

(b/deftask hyphenate-html
  "Hyphenate html files by replacing contents of text nodes with contents
  containg soft-hyphens (&shy;).

  Default language is \"en-us\". Use language option to select alternative
  language: https://github.com/Deraen/clj-hyphenate/tree/master/src/clj_hyphenate/patterns

  Use either filter to include only files matching or remove to
  include only files not matching regex."
  [f filter RE #{regex} "Regexes to filter HTML files"
   r remove RE #{regex} "Regexes to blacklist HTML files with"
   l language LANG str "Language"]
  (assert (or (and filter (not remove)) (and remove (not filter)))
          "Use either filter or remove.")
  (let [pod  (-> (b/get-env)
                 (update-in [:dependencies] into deps)
                 pod/make-pod
                 future)
        prev (atom nil)
        out  (b/tmp-dir!)
        language (or language "en-us")
        filter (cond
                 filter #(b/by-re filter %)
                 remove #(b/by-re remove % true)
                 :else identity)]
    (fn [next-task]
      (fn [fileset]
        (let [files (->> fileset
                         (b/fileset-diff @prev)
                         b/input-files
                         filter
                         (b/by-ext [".html"]))]
          (util/info "Hyphenating %d HTML files...\n" (count files))
          (doseq [file files
                  :let [new-file (io/file out (b/tmp-path file))]]
            (util/dbug "Hyphenating %s\n" (b/tmp-path file))
            (io/make-parents new-file)
            (pod/with-call-in @pod
              (deraen.boot-hyphenate.impl/hyphenate-html
                ~language
                ~(.getPath (b/tmp-file file))
                ~(.getPath new-file)))))
        (reset! prev fileset)
        (next-task (-> fileset (b/add-resource out) b/commit!))))))
