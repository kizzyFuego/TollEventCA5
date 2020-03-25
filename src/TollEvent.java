/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

/**
 *
 * @author kingsley Osemwenkhae D00215130
 */
public class TollEvent
{
    private String vehicleRegistration;
    private long ImageId;
    private Timestamp time;
    
    public TollEvent(String vehicleRegistration, long ImageId, Timestamp time  )
    {
        this.vehicleRegistration = vehicleRegistration;
        this.ImageId = ImageId;
        this.time = time;
    }
    
    public TollEvent(String vehicleRegistration, long ImageId  )
    {
        this.vehicleRegistration = vehicleRegistration;
        this.ImageId = ImageId;
        this.time = Timestamp.from(Instant.now());
    }

    public String getVehicleRegistration()
    {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration)
    {
        this.vehicleRegistration = vehicleRegistration;
    }

    public long getImageId()
    {
        return ImageId;
    }

    public void setImageId(long ImageId)
    {
        this.ImageId = ImageId;
    }

    public Timestamp getTime()
    {
        return time;
    }

    public void setTime(Timestamp time)
    {
        this.time = time;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.vehicleRegistration);
        hash = 67 * hash + (int) (this.ImageId ^ (this.ImageId >>> 32));
        hash = 67 * hash + Objects.hashCode(this.time);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final TollEvent other = (TollEvent) obj;
        if (this.ImageId != other.ImageId)
        {
            return false;
        }
        if (!Objects.equals(this.vehicleRegistration, other.vehicleRegistration))
        {
            return false;
        }
        if (!Objects.equals(this.time, other.time))
        {
            return false;
        }
        return true;
    }
    
    
    
    @Override
    public String toString()
    {
        return "TollEvent {Vehicle Registration=" + vehicleRegistration + ", ImageId=" + ImageId + ", time=" + time + '}';
    }
    
    
}
