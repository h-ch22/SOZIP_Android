package com.eje.sozip.userManagement.models

class AdditionalUserInfoViewModel {
    fun checkCompatibilitySchool(domain : String) : String?{
        when(domain){
            "kangwon.ac.kr" -> return "KANGWONNU"
            "knu.ac.kr" -> return "KNU"
            "gnu.ac.kr" -> return "GNU"
            "pusan.ac.kr" -> return "PNU"
            "snu.ac.kr" -> return "SNU"
            "jnu.ac.kr" -> return "JNU"
            "jbnu.ac.kr" -> return "JBNU"
            "jejunu.ac.kr" -> return "JEJUNU"
            "cnu.ac.kr" -> return "CNU"
            "chungbuk.ac.kr" -> return "CBNU"
            else -> return null
        }
    }

    fun getSchoolName(email : String) : String?{
        val domain = email.split("@")[1]
        val school_init = checkCompatibilitySchool(domain)

        if(school_init == null){
            return null
        }

        when(school_init){
            "KANGWONNU" -> return "강원대학교"
            "KNU" -> return "경북대학교"
            "GNU" -> return "경상국립대학교"
            "PNU" -> return "부산대학교"
            "SNU" -> return "서울대학교"
            "JNU" -> return "전남대학교"
            "JBNU" -> return "전북대학교"
            "JEJUNU" -> return "제주대학교"
            "CNU" -> return "충남대학교"
            "CBNU" -> return "충북대학교"
            else -> return null
        }
    }
}