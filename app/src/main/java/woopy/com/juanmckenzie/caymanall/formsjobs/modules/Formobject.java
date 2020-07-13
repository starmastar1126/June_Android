package woopy.com.juanmckenzie.caymanall.formsjobs.modules;

import android.widget.EditText;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import lib.kingja.switchbutton.SwitchMultiButton;
import woopy.com.juanmckenzie.caymanall.Objects.ImageObj;
import woopy.com.juanmckenzie.caymanall.Objects.User;

public class Formobject extends BaseObservable {


    private User user;
    private Boolean compelted = false;
    private String AddId = "";
    private ImageObj Profile;
    @Bindable
    private String firstname, Id, lastname, middlename, StreetAddress, ApartmentUnit,
            City, State, ZIPCode, Phone, Email, DateAvailable,
            Desredsallery, PositonforApplied, hname, haddress, hfrom, hto, hdeploma, cname, caddress,
            cfrom, cto, cdeploma, oname, oaddress, ofrom, oto, odeploma,
            rfullnmae1, rrelationship1, rcompany1, rphone1, raddress1, rfullnmae2, rrelationship2, rcompany2, rphone2,
            raddress2, rfullnmae3, rrelationship3, rcompany3, rphone3, raddress3,
            pename, pephone, peaddress, pesupervisor, pestartingsallery, peendingsallery, peresponsibilities, pefromdate,
            peenddate, pereasonforleaving, pejobtittle, mname, mfrom, mto, mrankofdischarge, mtypeofdischarge, mifotherthem, createdat;

    public ImageObj getProfile() {
        return Profile;
    }

    public void setProfile(ImageObj profile) {
        Profile = profile;
    }

    public String getAddId() {
        return AddId;
    }

    public void setAddId(String addId) {
        AddId = addId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }


    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getCompelted() {
        return compelted;
    }

    public void setCompelted(Boolean compelted) {
        this.compelted = compelted;
    }

    @Bindable
    public String getMname() {
        return mname;
    }

    @Bindable
    public void setMname(String mname) {
        this.mname = mname;
    }

    @Bindable
    public String getMfrom() {
        return mfrom;
    }

    @Bindable
    public void setMfrom(String mfrom) {
        this.mfrom = mfrom;
    }

    @Bindable
    public String getMto() {
        return mto;
    }

    @Bindable
    public void setMto(String mto) {
        this.mto = mto;
    }

    @Bindable
    public String getMrankofdischarge() {
        return mrankofdischarge;
    }

    @Bindable
    public void setMrankofdischarge(String mrankofdischarge) {
        this.mrankofdischarge = mrankofdischarge;
    }

    @Bindable
    public String getMtypeofdischarge() {
        return mtypeofdischarge;
    }

    @Bindable
    public void setMtypeofdischarge(String mtypeofdischarge) {
        this.mtypeofdischarge = mtypeofdischarge;
    }

    @Bindable
    public String getMifotherthem() {
        return mifotherthem;
    }

    @Bindable

    public void setMifotherthem(String mifotherthem) {
        this.mifotherthem = mifotherthem;
    }

    @Bindable
    public String getPejobtittle() {
        return pejobtittle;
    }

    @Bindable
    public void setPejobtittle(String pejobtittle) {
        this.pejobtittle = pejobtittle;
    }

    @Bindable
    public String getPename() {
        return pename;
    }

    @Bindable
    public void setPename(String pename) {
        this.pename = pename;
    }

    @Bindable
    public String getPephone() {
        return pephone;
    }

    @Bindable
    public void setPephone(String pephone) {
        this.pephone = pephone;
    }

    @Bindable
    public String getPeaddress() {
        return peaddress;
    }

    @Bindable
    public void setPeaddress(String peaddress) {
        this.peaddress = peaddress;
    }

    @Bindable
    public String getPesupervisor() {
        return pesupervisor;
    }

    @Bindable
    public void setPesupervisor(String pesupervisor) {
        this.pesupervisor = pesupervisor;
    }

    @Bindable
    public String getPestartingsallery() {
        return pestartingsallery;
    }

    @Bindable
    public void setPestartingsallery(String pestartingsallery) {
        this.pestartingsallery = pestartingsallery;
    }

    @Bindable
    public String getPeendingsallery() {
        return peendingsallery;
    }

    @Bindable
    public void setPeendingsallery(String peendingsallery) {
        this.peendingsallery = peendingsallery;
    }

    @Bindable
    public String getPeresponsibilities() {
        return peresponsibilities;
    }

    @Bindable
    public void setPeresponsibilities(String peresponsibilities) {
        this.peresponsibilities = peresponsibilities;
    }

    @Bindable
    public String getPefromdate() {
        return pefromdate;
    }

    @Bindable
    public void setPefromdate(String pefromdate) {
        this.pefromdate = pefromdate;
    }

    @Bindable
    public String getPeenddate() {
        return peenddate;
    }

    @Bindable
    public void setPeenddate(String peenddate) {
        this.peenddate = peenddate;
    }

    @Bindable
    public String getPereasonforleaving() {
        return pereasonforleaving;
    }

    @Bindable
    public void setPereasonforleaving(String pereasonforleaving) {
        this.pereasonforleaving = pereasonforleaving;
    }

    @Bindable
    public String getRfullnmae1() {
        return rfullnmae1;
    }

    @Bindable
    public void setRfullnmae1(String rfullnmae1) {
        this.rfullnmae1 = rfullnmae1;
    }

    @Bindable
    public String getRrelationship1() {
        return rrelationship1;
    }

    @Bindable
    public void setRrelationship1(String rrelationship1) {
        this.rrelationship1 = rrelationship1;
    }

    @Bindable
    public String getRcompany1() {
        return rcompany1;
    }

    @Bindable
    public void setRcompany1(String rcompany1) {
        this.rcompany1 = rcompany1;
    }

    @Bindable
    public String getRphone1() {
        return rphone1;
    }

    @Bindable
    public void setRphone1(String rphone1) {
        this.rphone1 = rphone1;
    }

    @Bindable
    public String getRaddress1() {
        return raddress1;
    }

    @Bindable
    public void setRaddress1(String raddress1) {
        this.raddress1 = raddress1;
    }

    @Bindable
    public String getRfullnmae2() {
        return rfullnmae2;
    }

    @Bindable
    public void setRfullnmae2(String rfullnmae2) {
        this.rfullnmae2 = rfullnmae2;
    }

    @Bindable
    public String getRrelationship2() {
        return rrelationship2;
    }

    @Bindable
    public void setRrelationship2(String rrelationship2) {
        this.rrelationship2 = rrelationship2;
    }

    @Bindable
    public String getRcompany2() {
        return rcompany2;
    }

    @Bindable
    public void setRcompany2(String rcompany2) {
        this.rcompany2 = rcompany2;
    }

    @Bindable
    public String getRphone2() {
        return rphone2;
    }

    @Bindable
    public void setRphone2(String rphone2) {
        this.rphone2 = rphone2;
    }

    @Bindable
    public String getRaddress2() {
        return raddress2;
    }

    @Bindable
    public void setRaddress2(String raddress2) {
        this.raddress2 = raddress2;
    }

    @Bindable
    public String getRfullnmae3() {
        return rfullnmae3;
    }

    @Bindable
    public void setRfullnmae3(String rfullnmae3) {
        this.rfullnmae3 = rfullnmae3;
    }

    @Bindable
    public String getRrelationship3() {
        return rrelationship3;
    }

    @Bindable
    public void setRrelationship3(String rrelationship3) {
        this.rrelationship3 = rrelationship3;
    }

    @Bindable
    public String getRcompany3() {
        return rcompany3;
    }

    @Bindable
    public void setRcompany3(String rcompany3) {
        this.rcompany3 = rcompany3;
    }

    @Bindable
    public String getRphone3() {
        return rphone3;
    }

    @Bindable
    public void setRphone3(String rphone3) {
        this.rphone3 = rphone3;
    }

    @Bindable
    public String getRaddress3() {
        return raddress3;
    }

    @Bindable
    public void setRaddress3(String raddress3) {
        this.raddress3 = raddress3;
    }

    @Bindable
    public String getHname() {
        return hname;
    }

    @Bindable
    public void setHname(String hname) {
        this.hname = hname;
    }

    @Bindable
    public String getHaddress() {
        return haddress;
    }

    @Bindable
    public void setHaddress(String haddress) {
        this.haddress = haddress;
    }

    @Bindable
    public String getHfrom() {
        return hfrom;
    }

    @Bindable
    public void setHfrom(String hfrom) {
        this.hfrom = hfrom;
    }

    @Bindable
    public String getHto() {
        return hto;
    }

    @Bindable
    public void setHto(String hto) {
        this.hto = hto;
    }

    @Bindable
    public String getHdeploma() {
        return hdeploma;
    }

    @Bindable
    public void setHdeploma(String hdeploma) {
        this.hdeploma = hdeploma;
    }

    @Bindable
    public String getCname() {
        return cname;
    }

    @Bindable
    public void setCname(String cname) {
        this.cname = cname;
    }

    @Bindable
    public String getCaddress() {
        return caddress;
    }

    @Bindable
    public void setCaddress(String caddress) {
        this.caddress = caddress;
    }

    @Bindable
    public String getCfrom() {
        return cfrom;
    }

    @Bindable
    public void setCfrom(String cfrom) {
        this.cfrom = cfrom;
    }

    @Bindable
    public String getCto() {
        return cto;
    }

    @Bindable
    public void setCto(String cto) {
        this.cto = cto;
    }

    @Bindable
    public String getCdeploma() {
        return cdeploma;
    }

    @Bindable
    public void setCdeploma(String cdeploma) {
        this.cdeploma = cdeploma;
    }

    @Bindable
    public String getOname() {
        return oname;
    }

    @Bindable
    public void setOname(String oname) {
        this.oname = oname;
    }

    @Bindable
    public String getOaddress() {
        return oaddress;
    }

    @Bindable
    public void setOaddress(String oaddress) {
        this.oaddress = oaddress;
    }

    @Bindable
    public String getOfrom() {
        return ofrom;
    }

    @Bindable
    public void setOfrom(String ofrom) {
        this.ofrom = ofrom;
    }

    @Bindable
    public String getOto() {
        return oto;
    }

    @Bindable
    public void setOto(String oto) {
        this.oto = oto;
    }

    @Bindable
    public String getOdeploma() {
        return odeploma;
    }

    @Bindable
    public void setOdeploma(String odeploma) {
        this.odeploma = odeploma;
    }

    @Bindable
    public int getQuestion1from2() {
        return question1from2;
    }

    @Bindable
    public void setQuestion1from2(int question1from2) {
        this.question1from2 = question1from2;
    }

    @Bindable
    public int getQuestion2from2() {
        return question2from2;
    }

    @Bindable
    public void setQuestion2from2(int question2from2) {
        this.question2from2 = question2from2;
    }

    @Bindable
    public int getQuestion3from2() {
        return question3from2;
    }

    @Bindable
    public void setQuestion3from2(int question3from2) {
        this.question3from2 = question3from2;
    }

    @Bindable
    private int question1from1 = 0, question2from1 = 0, question3from1 = 0, question4from1 = 0, question1from2 = 0, question2from2 = 0, question3from2 = 0;

    @Bindable
    public int getQuestion1from1() {
        return question1from1;
    }

    @Bindable
    public String getPhone() {
        return Phone;
    }

    @Bindable
    public void setPhone(String phone) {
        Phone = phone;
    }

    @Bindable
    public String getEmail() {
        return Email;
    }

    @Bindable
    public void setEmail(String email) {
        Email = email;
    }

    @Bindable
    public String getDateAvailable() {
        return DateAvailable;
    }

    @Bindable
    public void setDateAvailable(String dateAvailable) {
        DateAvailable = dateAvailable;
    }

    @Bindable
    public String getDesredsallery() {
        return Desredsallery;
    }

    @Bindable
    public void setDesredsallery(String desredsallery) {
        Desredsallery = desredsallery;
    }

    @Bindable
    public String getPositonforApplied() {
        return PositonforApplied;
    }

    @Bindable
    public void setPositonforApplied(String positonforApplied) {
        PositonforApplied = positonforApplied;
    }

    @Bindable
    public void setQuestion1from1(int question1from1) {
        this.question1from1 = question1from1;
    }

    @Bindable
    public int getQuestion2from1() {
        return question2from1;
    }

    @Bindable
    public void setQuestion2from1(int question2from1) {
        this.question2from1 = question2from1;
    }

    @Bindable
    public int getQuestion3from1() {
        return question3from1;
    }

    @Bindable
    public void setQuestion3from1(int question3from1) {
        this.question3from1 = question3from1;
    }

    @Bindable
    public int getQuestion4from1() {
        return question4from1;
    }

    @Bindable
    public void setQuestion4from1(int question4from1) {
        this.question4from1 = question4from1;
    }

    @Bindable
    public String getFirstname() {
        return firstname;
    }

    @Bindable
    public void setFirstname(String firstname) {
        this.firstname = firstname;
        notifyPropertyChanged(BR.firstname);
    }

    @Bindable
    public String getLastname() {
        return lastname;
    }

    @Bindable
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Bindable
    public String getMiddlename() {
        return middlename;
    }

    @Bindable
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    @Bindable
    public String getStreetAddress() {
        return StreetAddress;
    }

    @Bindable
    public void setStreetAddress(String streetAddress) {
        StreetAddress = streetAddress;
    }

    @Bindable
    public String getApartmentUnit() {
        return ApartmentUnit;
    }

    @Bindable
    public void setApartmentUnit(String apartmentUnit) {
        ApartmentUnit = apartmentUnit;
    }

    @Bindable
    public String getCity() {
        return City;
    }

    @Bindable
    public void setCity(String city) {
        City = city;
    }

    @Bindable
    public String getState() {
        return State;
    }

    @Bindable
    public void setState(String state) {
        State = state;
    }

    @Bindable
    public String getZIPCode() {
        return ZIPCode;
    }

    @Bindable
    public void setZIPCode(String ZIPCode) {
        this.ZIPCode = ZIPCode;
    }
}
