SELECT A.REC_NUM_REC FROM VSAM.PJY0B_RECNUM_ACC_LA A, VSAM.PJY0B_WORD_ACCOUNT_LA B WHERE B.LAST_WORD IN ('MIKE', 'WOOD') AND
    A.DBKEY BETWEEN FIRST_REC AND FIRST_REC+REC_COUNT - 1 GROUP BY A.REC_NUM_REC HAVING COUNT(*) = 2;
 
SELECT * FROM VSAM.PJY0B_MASTER_ACC_LA WHERE DBKEY IN (SELECT A.REC_NUM_REC FROM VSAM.PJY0B_RECNUM_ACC_LA A, VSAM.PJY0B_WORD_ACCOUNT_LA B WHERE B.LAST_WORD IN ('MIKE', 'WOOD') AND
    A.DBKEY BETWEEN FIRST_REC AND FIRST_REC+REC_COUNT - 1 GROUP BY A.REC_NUM_REC HAVING COUNT(*) = 2 );

SELECT * FROM VSAM.PJY0B_MASTER_ACC_LA WHERE DBKEY IN (SELECT A.REC_NUM_REC FROM VSAM.PJY0B_RECNUM_ACC_LA A, VSAM.PJY0B_WORD_ACCOUNT_LA B WHERE B.LAST_WORD IN ('JOHN', 'WOOD') AND
    A.DBKEY BETWEEN B.FIRST_REC AND B.FIRST_REC+B.REC_COUNT - 1 GROUP BY A.REC_NUM_REC HAVING COUNT(*) = 2 )
UNION
SELECT * FROM VSAM.PJY0B_MASTER_ACC_LA WHERE DBKEY IN (SELECT A.REC_NUM_REC FROM VSAM.PJY0B_RECNUM_ACC_LA A, VSAM.PJY0B_WORD_ACCOUNT_LA B WHERE B.LAST_WORD IN ('MIKE', 'WOOD') AND
    A.DBKEY BETWEEN B.FIRST_REC AND B.FIRST_REC+B.REC_COUNT - 1 GROUP BY A.REC_NUM_REC HAVING COUNT(*) = 2 )
UNION
SELECT * FROM VSAM.PJY0B_MASTER_ACC_LA WHERE DBKEY IN (SELECT A.REC_NUM_REC FROM VSAM.PJY0B_RECNUM_ACC_LA A, VSAM.PJY0B_WORD_ACCOUNT_LA B WHERE B.LAST_WORD IN ('MARK', 'WOOD') AND
    A.DBKEY BETWEEN B.FIRST_REC AND B.FIRST_REC+B.REC_COUNT - 1 GROUP BY A.REC_NUM_REC HAVING COUNT(*) = 2 )
 
SELECT A.REC_NUM_REC, 1 FROM VSAM.PJY0B_RECNUM_ACC_LA A, VSAM.PJY0B_WORD_ACCOUNT_LA B WHERE B.LAST_WORD IN ('JOHN', 'WOOD') AND

    A.DBKEY BETWEEN B.FIRST_REC AND B.FIRST_REC+B.REC_COUNT - 1 GROUP BY A.REC_NUM_REC HAVING COUNT(*) = 2  

UNION

SELECT A.REC_NUM_REC, 2 FROM VSAM.PJY0B_RECNUM_ACC_LA A, VSAM.PJY0B_WORD_ACCOUNT_LA B WHERE B.LAST_WORD IN ('MIKE', 'WOOD') AND

    A.DBKEY BETWEEN B.FIRST_REC AND B.FIRST_REC+B.REC_COUNT - 1 GROUP BY A.REC_NUM_REC HAVING COUNT(*) = 2 

UNION

SELECT A.REC_NUM_REC, 3 FROM VSAM.PJY0B_RECNUM_ACC_LA A, VSAM.PJY0B_WORD_ACCOUNT_LA B WHERE B.LAST_WORD IN ('MARK', 'WOOD') AND

    A.DBKEY BETWEEN B.FIRST_REC AND B.FIRST_REC+B.REC_COUNT - 1 GROUP BY A.REC_NUM_REC HAVING COUNT(*) = 2

 