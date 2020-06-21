package com.xishi.user.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户资金流水校对表
 * </p>
 *
 * @author stylefeng
 * @since 2019-06-19
 */
@TableName("eb_user_wallet_balance_record")
public class UserWalletBalanceRecord extends Model<UserWalletBalanceRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 钱包-牛角
     */
    @TableField("wallet_nj")
    private BigDecimal walletNj;
    /**
     * 钱包-牛气
     */
    @TableField("wallet_nq")
    private BigDecimal walletNq;
    /**
     * 钱包-余额
     */
    @TableField("wallet_money")
    private BigDecimal walletMoney;
    /**
     * 流水总牛角
     */
    @TableField("record_nj")
    private BigDecimal recordNj;
    /**
     * 流水总牛气
     */
    @TableField("record_nq")
    private BigDecimal recordNq;
    /**
     * 流水总余额
     */
    @TableField("record_money")
    private BigDecimal recordMoney;
    /**
     * 牛角：(钱包-流水)/钱包
     */
    private BigDecimal nj;
    /**
     * 牛气：(钱包-流水)/钱包
     */
    private BigDecimal nq;
    /**
     * 余额：(钱包-流水)/钱包
     */
    private BigDecimal money;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getWalletNj() {
        return walletNj;
    }

    public void setWalletNj(BigDecimal walletNj) {
        this.walletNj = walletNj;
    }

    public BigDecimal getWalletNq() {
        return walletNq;
    }

    public void setWalletNq(BigDecimal walletNq) {
        this.walletNq = walletNq;
    }

    public BigDecimal getWalletMoney() {
        return walletMoney;
    }

    public void setWalletMoney(BigDecimal walletMoney) {
        this.walletMoney = walletMoney;
    }

    public BigDecimal getRecordNj() {
        return recordNj;
    }

    public void setRecordNj(BigDecimal recordNj) {
        this.recordNj = recordNj;
    }

    public BigDecimal getRecordNq() {
        return recordNq;
    }

    public void setRecordNq(BigDecimal recordNq) {
        this.recordNq = recordNq;
    }

    public BigDecimal getRecordMoney() {
        return recordMoney;
    }

    public void setRecordMoney(BigDecimal recordMoney) {
        this.recordMoney = recordMoney;
    }

    public BigDecimal getNj() {
        return nj;
    }

    public void setNj(BigDecimal nj) {
        this.nj = nj;
    }

    public BigDecimal getNq() {
        return nq;
    }

    public void setNq(BigDecimal nq) {
        this.nq = nq;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserWalletBalanceRecord{" +
        "id=" + id +
        ", walletNj=" + walletNj +
        ", walletNq=" + walletNq +
        ", walletMoney=" + walletMoney +
        ", recordNj=" + recordNj +
        ", recordNq=" + recordNq +
        ", recordMoney=" + recordMoney +
        ", nj=" + nj +
        ", nq=" + nq +
        ", money=" + money +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
