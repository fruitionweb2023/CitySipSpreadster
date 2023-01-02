package com.arp.citysipspreadster.utils

import com.arp.citysipspreadster.model.notification.Notification
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec

object WS_URL_PARAMS {
    var service_selection_text = ""
    var status = ""
    var access_key = "90336"
    var issuer = "eKart"
    var subject = "eKart Authentication"
    private var JWT_KEY = "replace_with_your_strong_jwt_secret_key"

    var KEY_URL = "http://direct2web.in/forest/ws/"

    var cartItems: ArrayList<Notification> = ArrayList<Notification>()

    var msg: String? = null

    fun createJWT(issuer: String?, subject: String?): String? {
        try {
            val signatureAlgorithm = SignatureAlgorithm.HS256
            val nowMillis = System.currentTimeMillis()
            val now = Date(nowMillis)
            val apiKeySecretBytes = JWT_KEY.toByteArray()
            val signingKey: Key = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)
            val builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey)
            return builder.compact()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}