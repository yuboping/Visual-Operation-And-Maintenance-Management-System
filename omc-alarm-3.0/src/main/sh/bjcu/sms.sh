#!/bin/sh

# the next line restarts using tclsh \
exec tclsh "$0" "$@"
set SMSGATE_IP "132.77.95.131"
set SMSGATE_PORT 8121
set TCP_TIMEOUT 5

set AIIP(COMM,CRYPTEDPASSWD) 0                  

proc openlogfile {} {
        set fn ./checkswapinfo.log
        if { [catch {open $fn a+} fp] } {
                return  stdout
        }
        return  $fp
}

proc write_log { arg_message } {
        set logfp [openlogfile]
        set msg_prefix "[clock format [clock seconds] -format {%Y%m%d %H:%M:%S}] \[[pid]\] "
        puts $logfp "$msg_prefix $arg_message"
        if { $logfp != "stdout" } {
                close $logfp
        }
}

proc readline {sock} {
  global TCP_TIMEOUT
        set socklist [select $sock "" "" $TCP_TIMEOUT]
  if {$socklist == ""} {
    return ""
  }
  set res [gets $sock]
  return $res
}

proc send_cmd { sip sport cmd } {
        set s [socket $sip $sport]
        fconfigure $s -buffering line
        puts $s $cmd
        set line [readline $s]
        if {$line == ""} {
    close $s
    error "Timeout when receive the response from  $sip : $sport"
        }
        flush $s
        close $s
        return $line
}

proc Action_WlanSendSM {sendto sendmsg} {
        global SMSGATE_IP 
        global SMSGATE_PORT 

  set cmd "Action_WXCSSendSM {$sendto} {$sendmsg}"
  if {[catch {send_cmd $SMSGATE_IP $SMSGATE_PORT $cmd} err_msg] == 1} {
          #write_log "SendMsg failed! cmd=$cmd, err=$err_msg,"
          error "SendMsg failed! $err_msg"
  }
  write_log "SendMsg OK: cmd=$cmd"
  return "OK"
}
        
write_log "WlanMACNotify begin"

set username [lindex $argv 0]
set sendmsg [lindex $argv 1]

write_log "WlanSendSM {$username} {$sendmsg}"
catch {eval Action_WlanSendSM {$username} {$sendmsg}} 
  
write_log "WlanMACNotify end"

exit 0