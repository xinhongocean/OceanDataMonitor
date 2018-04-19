hd=`df -lh|grep /xinhong|awk '{print $3,$4,$5}'`
load=`uptime | awk -F 'load average: ' '{print $2}'`
cpufree=`vmstat 1 5 |sed -n '3,$p' |awk '{x = x + $15} END {print x/5}' |awk -F. '{print $1}'`
echo $hd
echo $load
echo $cpufree
