fun_mean <- function(x){return(data.frame(y=round(mean(x),2),label=round(mean(x,na.rm=T),2)))}
ggplot(data=test_allData_ENERGY, aes(x=1:60,y=value, fill=1:60))+
geom_bar(stat="identity") +
stat_summary(fun.data = fun_mean, geom="text", vjust=1.5, size=3.2)+
theme_classic()  + theme(text = element_text(size =11.0)) +
 ggh4x::facet_wrap2(
factor(benchmark, levels = c("localLogin","localLoginEnc")) ~., scales = "free_y", ncol=1)
theme(axis.text.x = element_text(angle=90))

ggplot(data=test_allData_FOREGROUND_TIME, aes(x=framework,y=value, fill=framework))+
geom_bar(stat="summary", fun="mean") +
stat_summary(fun.data = fun_mean, geom="text", vjust=1.5, size=3.2)+
theme_classic()  + theme(text = element_text(size =11.0)) +
 ggh4x::facet_wrap2(
factor(benchmark, levels = c("localLogin","localLoginEnc")) ~., scales = "free_y", ncol=1)
theme(axis.text.x = element_text(angle=90))

ggplot(data=test_allData_CPU_TIME, aes(x=framework,y=value, fill=framework))+
geom_bar(stat="summary", fun="mean") +
stat_summary(fun.data = fun_mean, geom="text", vjust=1.5, size=3.2)+
theme_classic()  + theme(text = element_text(size =11.0)) +
 ggh4x::facet_wrap2(
factor(benchmark, levels = c("localLogin","localLoginEnc")) ~., scales = "free_y", ncol=1)
theme(axis.text.x = element_text(angle=90))

ggplot(data=test_allData_MEMORY, aes(x=framework,y=value, fill=framework))+
geom_bar(stat="summary", fun="mean") +
stat_summary(fun.data = fun_mean, geom="text", vjust=1.5, size=3.2)+
theme_classic()  + theme(text = element_text(size =11.0)) +
 ggh4x::facet_wrap2(
factor(benchmark, levels = c("localLogin","localLoginEnc")) ~., scales = "free_y", ncol=1)
theme(axis.text.x = element_text(angle=90))

ggplot(data=test_allData_HEAP_SIZE, aes(x=framework,y=value, fill=framework))+
geom_bar(stat="summary", fun="mean") +
stat_summary(fun.data = fun_mean, geom="text", vjust=1.5, size=3.2)+
theme_classic()  + theme(text = element_text(size =11.0)) +
 ggh4x::facet_wrap2(
factor(benchmark, levels = c("localLogin","localLoginEnc")) ~., scales = "free_y", ncol=1)
theme(axis.text.x = element_text(angle=90))

ggplot(data=test_allData_HEAP_ALLOC, aes(x=framework,y=value, fill=framework))+
geom_bar(stat="summary", fun="mean") +
stat_summary(fun.data = fun_mean, geom="text", vjust=1.5, size=3.2)+
theme_classic()  + theme(text = element_text(size =11.0)) +
 ggh4x::facet_wrap2(
factor(benchmark, levels = c("localLogin","localLoginEnc")) ~., scales = "free_y", ncol=1)
theme(axis.text.x = element_text(angle=90))

ggplot(data=test_allData_HEAP_FREE, aes(x=framework,y=value, fill=framework))+
geom_bar(stat="summary", fun="mean") +
stat_summary(fun.data = fun_mean, geom="text", vjust=1.5, size=3.2)+
theme_classic()  + theme(text = element_text(size =11.0)) +
 ggh4x::facet_wrap2(
factor(benchmark, levels = c("localLogin","localLoginEnc")) ~., scales = "free_y", ncol=1)
theme(axis.text.x = element_text(angle=90))

