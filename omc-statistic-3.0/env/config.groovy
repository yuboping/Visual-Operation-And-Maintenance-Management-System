prefix='/home/lcims/omc/omc-statistic-3.0';
db{
    driver='com.mysql.cj.jdbc.Driver';
}
port=9089
sh{
    LANG='zh_CN.UTF-8';
    target='omc-statistic-3.0';
}
environments {
    dev{
        port=9088
    	db{
            driver='com.mysql.jdbc.Driver';
            url='jdbc:mysql://10.21.37.197:3306/omc_nmcu?useUnicode=true&amp;characterEncoding=utf-8';
            username='omcweb';
            password='omcweb';
            identity='MYSQL';
        }
        prefix='/home/lcims/omc/omc-statistic-3.0';
        sh{
        	LANG='zh_CN.utf8';
            path='/home/lcims/omc/omc-alarm-3.0/sh/start.sh';
        }
        JAVA_HOME='/home/lcims/app/jdk8';
    }
    demo{
        port=9108
        db{
            driver='com.mysql.jdbc.Driver';
            url='jdbc:mysql://10.21.37.197:3306/omc_demo?useUnicode=true&amp;characterEncoding=utf-8';
            username='omcweb';
            password='omcweb';
            identity='MYSQL';
        }
        prefix='/home/lcims/omcdemo/omc-statistic-3.0';
        sh{
            LANG='zh_CN.utf8';
            path='/home/lcims/omcdemo/omc-alarm-3.0/sh/start.sh';
        }
        JAVA_HOME='/home/lcims/app/jdk8';
    }
    sicu{
        db{
            driver='oracle.jdbc.OracleDriver'
            url='jdbc:oracle:thin:@218.26.146.13:1521:ORA11G';
            username='aimnt';
            password='Aisicuomc123#';
            identity='ORACLE';
        }
        prefix='/home/omc/omc-statistic';
        sh{
            LANG='zh_CN.utf8';
            path='/home/omc/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/home/omc/jdk8';
    }
	nmct{
    	db{
            driver='com.mysql.cj.jdbc.Driver'
            url='jdbc:mysql://192.168.100.80:3306/omc?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8';
            username='omc';
            password='nmobs%3A';
            identity='MYSQL';
        }
        prefix='/omc/omc-statistic-3.0';
        sh{
        	LANG='zh_CN.utf8';
            path='/omc/omc-alarm-3.0/sh/start.sh';
        }
        JAVA_HOME='/home/jdk1.8.0_181';
    }
	
	gscm{
    	db{
            driver='com.mysql.jdbc.Driver';
            url='jdbc:mysql://192.168.8.103:3306/gsomc?useUnicode=true&amp;characterEncoding=utf-8';
            username='gsomc';
            password='GSomc@0105';
            identity='MYSQL';
        }
        prefix='/home/omc/omc-statistic-3.0';
        sh{
        	LANG='zh_CN.utf8';
			path='/home/omc/omc-alarm-3.0/sh/start.sh';
        }
        JAVA_HOME='/home/omc/jdk8/jdk1.8.0_181';
    }
	hncmtest{
        db{
            driver='oracle.jdbc.OracleDriver'
            url='jdbc:oracle:thin:@10.96.207.164:1521:IPCS';
            username='nomc';
            password='as1a1nf0';
            identity='ORACLE';
        }
        prefix='/home/omc/omc-statistic-3.0';
        sh{
            LANG='zh_CN.utf8';
            path='/home/omc/omc-alarm-3.0/sh/start.sh';
        }
        JAVA_HOME='/home/omc/jdk8';
    }
	hncm{
	    port=9060
        db{
            driver='oracle.jdbc.OracleDriver'
            url='jdbc:oracle:thin:@10.218.224.188:1521:OBSDB';
            username='nomc';
            password='SML12-omc';
            identity='ORACLE';
        }
        prefix='/home/omc/omc_new/omc-statistic';
        sh{
            LANG='zh_CN.utf8';
            path='/home/omc/omc_new/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/home/radius/omc/jdk8';
    }
	sxcm{
        db{
            driver='oracle.jdbc.OracleDriver'
            url='jdbc:oracle:thin:@10.136.220.31:1521:LCIMS70DB';
            username='omcadmin';
            password='omcadmin70sn';
            identity='ORACLE';
        }
        prefix='/data/lcims/omc/omc-statistic';
        sh{
            LANG='zh_CN.utf8';
            path='/data/lcims/omc/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/data/lcims/omc/jdk8';
    }
    gdcu-test{
        port=9088
        db{
            driver='com.mysql.jdbc.Driver';
            url='jdbc:mysql://132.100.175.35:5200/omc?useUnicode=true&amp;characterEncoding=utf-8';
            username='omcweb';
            password='omcweb@123';
            identity='MYSQL';
        }
        prefix='/data3/aiomc/omc3.0/omc-statistic-3.0';
        sh{
            LANG='zh_CN.utf8';
            path='/data3/aiomc/omc3.0/omc-alarm-3.0/sh/start.sh';
        }
        JAVA_HOME='/data3/aiomc/omc3.0/jdk8';
    }
	gdcu{
        port=2327
        db{
            driver='com.mysql.jdbc.Driver';
            url='jdbc:mysql://132.98.3.200:2324/gdomc?useUnicode=true&amp;characterEncoding=utf-8';
            username='omcweb';
            password='OMCweb@123';
            identity='MYSQL';
        }
        prefix='/data/aiomc/omc3.0/omc-statistic';
        sh{
            LANG='zh_CN.utf8';
            path='/data/aiomc/omc3.0/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/data/aiomc/jdk8';
    }
	shcu{
	
        db{
            driver='com.mysql.jdbc.Driver';
            url='jdbc:mysql://134.77.104.40:3306/shomc?useUnicode=true&amp;characterEncoding=utf-8';
            username='omc';
            password='Shobs_156';
            identity='MYSQL';
        }
		 prefix='/home/omc/omc-statistic'
        sh{
            LANG='zh_CN.utf8';
            path='/home/omc/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/home/omc/jdk8'
	}
	jscu{
	    port=9004
        db{
            driver='com.mysql.jdbc.Driver';
            url='jdbc:mysql://10.25.80.135:3306/jscuomc?useUnicode=true&amp;characterEncoding=utf-8';
            username='jscuomc';
            password='JSCUomc@123';
            identity='MYSQL';
        }
		 prefix='/lcims/omc/omc-statistic'
        sh{
            LANG='zh_CN.utf8';
            path='/lcims/omc/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/lcims/omc/jdk8'
	}
	ahct{
        db{
            driver='oracle.jdbc.OracleDriver'
            url='jdbc:oracle:thin:@202.102.192.119:1721:BILL70';
            username='billadmin70';
            password='billadmin';
            identity='ORACLE';
        }
        prefix='/lcims/omc/omc-statistic';
        sh{
            LANG='zh_CN.utf8';
            path='/lcims/omc/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/lcims/omc/jdk8';
    }
	hncu{
	    port=18002
        db{
            driver='com.mysql.jdbc.Driver'
            url='jdbc:mysql://192.169.0.10:23306/OMC?useUnicode=true&amp;characterEncoding=utf-8';
            username='omcdev';
            password='DevCom109@';
            identity='MYSQL';
        }
		 prefix='/home/lciptv/omc/omc-statistic'
        sh{
            LANG='zh_CN.utf8';
            path='/home/lciptv/omc/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/home/lciptv/app/jdk8'
	}
	qhcm{
	    port=9052
        db{
            driver='oracle.jdbc.OracleDriver';
            url='jdbc:oracle:thin:@10.135.188.93:1521:CRM';
            username='ydtest80';
            password='ydtest';
            identity='ORACLE';
        }
		 prefix='/lcims/omc/omc-statistic'
        sh{
            LANG='zh_CN.utf8';
            path='/lcims/omc/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/lcims/omc/jdk8'
	}
	shcm{
	    port=9089
        db{
            driver='com.mysql.jdbc.Driver'
            url='jdbc:mysql://10.9.234.46:3306/OMC?useUnicode=true&amp;characterEncoding=utf-8';
            username='omc';
            password='OMC@wlan';
            identity='MYSQL';
        }
		 prefix='/home/OMC/omc-statistic'
        sh{
            LANG='zh_CN.utf8';
            path='/home/OMC/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/home/OMC/jdk8'
	}
	ahcm{
        port=9005
    	db{
            driver='com.mysql.jdbc.Driver';
            url='jdbc:mysql://10.147.202.106:3306/ahomc?useUnicode=true&amp;characterEncoding=utf-8';
            username='ahomc';
            password='AHCM#admin123';
            identity='MYSQL';
        }
        prefix='/lcims/omc/omc-statistic';
        sh{
        	LANG='zh_CN.utf8';
            path='/lcims/omc/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/lcims/omc/jdk8';
    }
	cqcm{
        port=9005
    	db{
            driver='com.mysql.jdbc.Driver';
            url='jdbc:mysql://192.168.81.53:23306/omcdb?useUnicode=true&amp;characterEncoding=utf-8';
            username='omcuser';
            password='7r@4S*9pEg';
            identity='MYSQL';
        }
        prefix='/aiiot/omc/omc-statistic-3.0';
        sh{
        	LANG='zh_CN.utf8';
            path='/aiiot/omc/omc-alarm/sh/start.sh';
        }
		province{
			name='cqcm'
		}
        JAVA_HOME='/usr';
    }
	gzcm{
	    port=62304 
        db{
            driver='oracle.jdbc.OracleDriver'
            url='jdbc:oracle:thin:@192.50.50.115:1521:aaa';
            username='aaa2omc';
            password='AaA#MaNag1Omc';
            identity='ORACLE';
        }
        prefix='/home/aiomc/omc/omc-statistic';
        sh{
            LANG='zh_CN.utf8';
            path='/home/aiomc/omc/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/home/aiomc/omc/jdk1.8';
    }
	gscu{
    	db{
            driver='com.mysql.jdbc.Driver';
            url='jdbc:mysql://10.1.1.18:3306/gsomc?useUnicode=true&amp;characterEncoding=utf-8';
            username='gsomc';
            password='GSomc@0105';
            identity='MYSQL';
        }
        prefix='/home/omc/omc-statistic';
        sh{
        	LANG='zh_CN.utf8';
			path='/home/omc/omc-alarm/sh/start.sh';
        }
        JAVA_HOME='/home/omc/jdk8/jdk1.8.0_181';
    }
	cqcmtest{
        port=9005
    	db{
            driver='com.mysql.jdbc.Driver';
            url='jdbc:mysql://192.168.106.119:3306/omcdb?useUnicode=true&amp;characterEncoding=utf-8';
            username='omcuser';
            password='omcuser@123';
            identity='MYSQL';
        }
        prefix='/aiiot/omc/omc-statistic-3.0';
        sh{
        	LANG='zh_CN.utf8';
            path='/iot/omc/omc-alarm/sh/start.sh';
        }
		province{
			name='cqcm'
		}
        JAVA_HOME='/usr';
    }
}