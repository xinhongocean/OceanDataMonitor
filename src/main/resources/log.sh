log=`tail -n $1 $2 | grep $3`
for id in $log
do
echo $id
done