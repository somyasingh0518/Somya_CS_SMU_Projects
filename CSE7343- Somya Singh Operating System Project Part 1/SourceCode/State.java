public enum State{
		RUNNING {public String toString(){return "RUNNING";}}, 
		WAITING {public String toString(){return "WAITING";}},
    HALTED  {public String toString(){return "HALTED";}},
    NEW     {public String toString(){return "NEW";}},
    TERMINATED{public String toString(){return "TERMINATED";}},
    READY   {public String toString(){return "READY";}};
}
