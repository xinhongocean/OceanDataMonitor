echo $1
if [ $1 -gt 0 ]
then
    echo 1
    nohup java -server -Xms512m -Xmx4096m -jar $2 >/dev/null &
elif [ $1 == 0 ]
then
   echo 0
   nohup python3 $2 &
else
   echo -1
   nohup sh $2 >/dev/null &

fi