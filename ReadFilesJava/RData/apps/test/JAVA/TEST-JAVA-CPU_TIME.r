ll1000_JAVA_ec = c(2.156,2.556,2.308,2.315,2.223,2.692,2.456,2.193,2.288,2.038,1.765,2.999,2.407,2.785,2.074,2.37,2.5,2.35,2.61,2.198,2.314,2.281,2.276,2.565,2.144,2.452,2.524,2.301,2.317,2.156)
lle1000_JAVA_ec = c(3.203,3.399,3.044,3.044,3.547,3.257,3.257,3.542,3.542,3.419,3.931,3.931,3.467,3.184,2.977,2.977,2.963,2.799,2.679,2.896,3.166,2.862,3.241,3.175,3.3,2.962,3.608,2.807,3.163,3.676)
par(mfrow=c(1,2))
barplot(ll1000_JAVA_ec, main=paste('localLogin-1000 avg:' ,mean(ll1000_JAVA_ec)))
barplot(lle1000_JAVA_ec, main=paste('localLoginEnc-1000 avg:' ,mean(lle1000_JAVA_ec)))
ll1000_JAVA_ec_Data = c(mean(ll1000_JAVA_ec),sd(ll1000_JAVA_ec))
lle1000_JAVA_ec_Data = c(mean(lle1000_JAVA_ec),sd(lle1000_JAVA_ec))
ll1000_JAVA_ec_Data
lle1000_JAVA_ec_Data
