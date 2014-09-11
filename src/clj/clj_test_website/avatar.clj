(ns clj-test-website.avatar
  (:import (java.util Random)
           (java.awt Color)
           (java.io File IOException)
           (java.awt.image BufferedImage)
           (javax.imageio ImageIO)))

(defn ^:private char-to-int
  [s]
  (Character/codePointAt (str s) 0))

(defn ^:private djb2
  [^String to-hash]
  (let [magic-constant 5381]
    (reduce (fn [h c]
              (+ (* 33 h) (char-to-int c)))
            magic-constant
            (take 8 to-hash))))

(defn ^:private make-2d-vec
  [size]
  (zipmap
    (map (comp keyword str) (range size))
    (map (fn [_] (vec (range size))) (vec (range size)))))

(defn ^:private getv
  [avatar row col]
  (get (avatar (keyword (str row))) col))

(defn ^:private setv
  [avatar row col val]
  (update-in avatar [(keyword (str row))] (fn [v] (assoc v col val))))

(defn ^:private write-image
  [user-name seed-str avatar]
  (let [avatar-size (count avatar)
        pixel-size 25
        file-name (str "/images/avatars/"
                       avatar-size "_" seed-str "_" (apply str user-name) ".png")
        out-file (File. (str "resources/public" file-name))
        the-image (BufferedImage. (* pixel-size avatar-size)
                                  (* pixel-size avatar-size)
                                  BufferedImage/TYPE_INT_RGB)]
    (doseq [x (range avatar-size)
            y (range avatar-size)
            px (range pixel-size)
            py (range pixel-size)]
      (.setRGB the-image
               (+ (* pixel-size x) px)
               (+ (* pixel-size y) py)
               (getv avatar x y)))
    (try
      (println out-file)
      (ImageIO/write the-image "png" out-file)
      (catch IOException e
        (do (println str (.getMessage e))
            (str (.getMessage e)))))
    file-name))

(defn create-avatar
  [user-name seed-str avatar-size]
  (let [^Random random (Random. (+ (djb2 seed-str) (djb2 user-name)))
        avatar (atom (make-2d-vec avatar-size))
        H (mod (Math/abs (.nextFloat random)) 1.0)
        S (+ (mod (Math/abs (.nextFloat random)) 0.5) 0.5)
        B (+ (mod (Math/abs (.nextFloat random)) 0.3) 0.7)
        fore-color (.getRGB (Color. 50 50 50))]
    (doseq [row (range (/ avatar-size 2))
            col (range avatar-size)
            :let [mod1 (- (mod (Math/abs (.nextFloat random)) 0.1) 0.05)
                  mod2 (- (mod (Math/abs (.nextFloat random)) 0.1) 0.05)
                  choice (.nextBoolean random)
                  color1 (if choice
                          (Color/HSBtoRGB (+ H mod1) S (min (max (+ B mod1) 0.0) 1.0))
                          fore-color)
                  color2 (if choice
                           (Color/HSBtoRGB (+ H mod2) S (min (max (+ B mod2) 0.0) 1.0))
                           fore-color)]]
      (swap! avatar (fn [oldval]
                      (-> oldval
                          (setv row col
                                color1)
                          (setv (- avatar-size row 1) col
                                color2)))))
    (write-image user-name seed-str @avatar)))

(def ^:private alphabet (vec "abcdefghijklmnopqrstuvwxyz"))

(defn main
  [n size]
  (map (fn [name] (create-avatar name "clojure" size))
       (map (fn [_] (take 8 (shuffle alphabet))) (range n))))