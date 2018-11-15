package com.cisco.user.services;


import com.cisco.user.models.DataBukti;
import com.cisco.user.models.InsertValue;
import com.cisco.user.models.IsiPromosi;
import com.cisco.user.models.Jadwal;
import com.cisco.user.models.Kelas;
import com.cisco.user.models.Promosi;
import com.cisco.user.models.User;
import com.cisco.user.models.Value;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServices {
    @FormUrlEncoded
    @POST("signin.php")
    Call<Value<User>> signin(@Field("xkey") String xkey,
                             @Field("nim") String nim,
                             @Field("password") String password,
                             @Field("token") String token);

    @FormUrlEncoded
    @POST("promosi.php")
    Call<Value<Promosi>> getpromosi(@Field("xkey") String xkey);

    @FormUrlEncoded
    @POST("isipromosi.php")
    Call<Value<IsiPromosi>> getisipromosi(@Field("xkey") String xkey, @Field("id") String id);

    @FormUrlEncoded
    @POST("jadwalkelas.php")
    Call<Value<Jadwal>> getjadwalkelas(@Field("xkey") String xkey,
                                       @Field("nim") String nim);

    @FormUrlEncoded
    @POST("registrasi.php")
    Call<Value<InsertValue>> signup(@Field("xkey") String xkey,
                                    @Field("nim") String nim,
                                    @Field("firstname") String firstname,
                                    @Field("lastname") String lastname,
                                    @Field("email") String email,
                                    @Field("prodi") String prodi,
                                    @Field("password") String password,
                                    @Field("imageName") String imageName,
                                    @Field("gambar") String gambar);

    @FormUrlEncoded
    @POST("namakelas.php")
    Call<Value<Kelas>> getnamakelas(@Field("xkey") String xkey,
                                    @Field("tanggal") String tanggal);

    @FormUrlEncoded
    @POST("ambilkelas.php")
    Call<Value<InsertValue>> setkelas(@Field("xkey") String xkey,
                                      @Field("nim") String nim,
                                      @Field("idinstrukturkelas") String idinstrukturkelas,
                                      @Field("imageName") String imageName,
                                      @Field("gambar") String gambar,
                                      @Field("statuspembayaran") String statuspembayaran,
                                      @Field("harga") String harga);

    @FormUrlEncoded
    @POST("getbukti.php")
    Call<Value<DataBukti>> getbukti(@Field("xkey") String xkey,
                                    @Field("nim") String nim);

    @FormUrlEncoded
    @POST("uploadbukti.php")
    Call<Value<InsertValue>> setuploadbukti(@Field("xkey") String xkey,
                                            @Field("nim") String nim,
                                            @Field("imageName") String imageName,
                                            @Field("gambar") String gambar);

}